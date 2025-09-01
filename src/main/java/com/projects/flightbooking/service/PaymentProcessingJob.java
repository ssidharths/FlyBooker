package com.projects.flightbooking.service;

import com.projects.flightbooking.entity.Booking;
import com.projects.flightbooking.entity.BookingSeat;
import com.projects.flightbooking.entity.Payment;
import com.projects.flightbooking.entity.Seat;
import com.projects.flightbooking.entity.enums.BookingStatus;
import com.projects.flightbooking.entity.enums.PaymentStatus;
import com.projects.flightbooking.entity.enums.SeatStatus;
import com.projects.flightbooking.repository.BookingRepository;
import com.projects.flightbooking.repository.PaymentRepository;
import com.projects.flightbooking.repository.SeatRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentProcessingJob {

    private static final Logger logger = LoggerFactory.getLogger(PaymentProcessingJob.class);

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private SeatService seatService;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private FlightService flightService;

    @Autowired
    private PaymentService paymentService;

    @Scheduled(fixedRate = 30000) // Every 30 seconds
    public void processPendingPayments() {
        logger.info("Starting scheduled payment processing job");

        List<Payment> pendingPayments = paymentRepository.findByPaymentStatus(PaymentStatus.PENDING);
        logger.info("Found {} pending payments to process", pendingPayments.size());

        for (Payment payment : pendingPayments) {
            try {
                processSinglePayment(payment);
            } catch (Exception e) {
                logger.error("Failed to process payment {}: {}", payment.getTransactionId(), e.getMessage());
            }
        }

        logger.info("Scheduled payment processing job completed");
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void processSinglePayment(Payment payment) {
        try {
            logger.info("Processing payment {} for booking {}", payment.getTransactionId(),
                    payment.getBooking().getBookingReference());

            // Process payment
            Payment processedPayment = paymentService.processPayment(payment.getTransactionId());

            if (processedPayment.getPaymentStatus() == PaymentStatus.COMPLETED) {
                // Payment successful - confirm booking
                Booking booking = payment.getBooking();
                booking.setStatus(BookingStatus.CONFIRMED);
                booking.getPayment().setPaymentStatus(PaymentStatus.COMPLETED);
                bookingRepository.save(booking);

                // Update seat status to OCCUPIED
                List<BookingSeat> bookingSeats = booking.getBookingSeats();
                for (BookingSeat bookingSeat : bookingSeats) {
                    Seat seat = bookingSeat.getSeat();
                    seat.setStatus(SeatStatus.OCCUPIED);
                    seatRepository.save(seat);
                }
                logger.info("Payment {} successful. Booking {} confirmed",
                        payment.getTransactionId(), booking.getBookingReference());
            } else {
                // Payment failed - cancel booking and release seats
                handlePaymentFailure(payment);
            }
        } catch (Exception e) {
            logger.error("Error processing payment {}: {}", payment.getTransactionId(), e.getMessage());
            handlePaymentFailure(payment);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    private void handlePaymentFailure(Payment payment) {
        try {
            // Set payment status to FAILED
            payment.setPaymentStatus(PaymentStatus.FAILED);
            paymentRepository.save(payment);

            // Cancel booking
            Booking booking = payment.getBooking();
            booking.setStatus(BookingStatus.CANCELLED);
            bookingRepository.save(booking);

            // Release seats
            List<BookingSeat> bookingSeats = booking.getBookingSeats();
            List<Long> seatIds = bookingSeats.stream()
                    .map(bs -> bs.getSeat().getId())
                    .collect(Collectors.toList());

            seatService.releaseSeats(seatIds);

            // Update flight available seats
            flightService.updateAvailableSeats(booking.getFlight().getId(), seatIds.size());

            logger.info("Payment {} failed. Booking {} cancelled and seats released",
                    payment.getTransactionId(), booking.getBookingReference());
        } catch (Exception e) {
            logger.error("Error handling payment failure for payment {}: {}",
                    payment.getTransactionId(), e.getMessage());
        }
    }
}