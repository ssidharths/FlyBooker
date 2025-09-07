package com.projects.flightbooking.service;

import com.projects.flightbooking.dto.booking.BookingRequest;
import com.projects.flightbooking.dto.booking.BookingResponse;
import com.projects.flightbooking.entity.*;
import com.projects.flightbooking.entity.enums.BookingStatus;
import com.projects.flightbooking.entity.enums.PaymentMethod;
import com.projects.flightbooking.repository.BookingRepository;
import com.projects.flightbooking.repository.BookingSeatRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BookingService {
    private static final Logger logger = LoggerFactory.getLogger(BookingService.class);
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private BookingSeatRepository bookingSeatRepository;
    @Autowired
    private FlightService flightService;
    @Autowired
    private SeatService seatService;
    @Autowired
    private PaymentService paymentService;

    @Transactional
    public BookingResponse createBooking(BookingRequest request) {
        logger.info("::Booking initiated by {}::", request.getPassengerEmail());
        Optional<Flight> flightOpt = flightService.getFlightById(request.getFlightId());
        if (flightOpt.isEmpty()) {
            throw new RuntimeException("Flight Not Found");
        }
        Flight retrievedFlight = flightOpt.get();

        //Check if seats are available
        for (Long seatId : request.getSeatIds()) {
            if (!seatService.isSeatAvailable(seatId)) {
                throw new RuntimeException("One or more seats are not available");
            }
        }
        BigDecimal totalAmount = calculateTotalAmount(request.getSeatIds(), retrievedFlight);
        String bookingReference = generateBookingReference();

        // Create booking with PENDING status
        Booking booking = new Booking(bookingReference, request.getPassengerName(),
                request.getPassengerEmail(), request.getPassengerPhone(),
                totalAmount, retrievedFlight);
        booking.setStatus(BookingStatus.PENDING);
        booking = bookingRepository.save(booking);

        // Reserve seats
        seatService.reserveSeats(request.getSeatIds());

        // Generating the Booking-Seat Relationship
        for (Long seatId : request.getSeatIds()) {
            Optional<Seat> seatOpt = seatService.getSeatById(seatId);
            if (seatOpt.isPresent()) {
                BookingSeat bookingSeat = new BookingSeat(booking, seatOpt.get());
                bookingSeatRepository.save(bookingSeat);
            }
        }
        // Update flight available seats
        flightService.updateAvailableSeats(retrievedFlight.getId(), -request.getSeatIds().size());
        // Create payment record with PENDING status
        Payment payment = paymentService.createPayment(booking, PaymentMethod.valueOf(request.getPaymentMethod().toUpperCase()));
        logger.info("::Booking created successfully with PENDING status. Payment ID: {}::", payment.getTransactionId());
        logger.info("::Payment will be processed by scheduled job::");
        return convertToBookingResponse(booking);
    }

    public Optional<BookingResponse> getBookingByReference(String bookingReference) {
        Optional<Booking> booking = bookingRepository.findByBookingReference(bookingReference);
        return booking.map(this::convertToBookingResponse);
    }

    public List<BookingResponse> getBookingsByEmail(String email) {
        List<Booking> bookings = bookingRepository.findByPassengerEmailOrderByCreatedAtDesc(email);
        return bookings.stream().map(this::convertToBookingResponse).collect(Collectors.toList());
    }

    @Transactional
    public void cancelBooking(String bookingReference) {
        Optional<Booking> bookingOpt = bookingRepository.findByBookingReference(bookingReference);
        if (bookingOpt.isEmpty()) {
            throw new RuntimeException("Booking not found");
        }
        Booking booking = bookingOpt.get();
        logger.info("f::Cancel initiated for {} by {}::", booking.getFlight(), booking.getPassengerEmail());
        booking.setStatus(BookingStatus.CANCELLED);
        // Release seats
        List<BookingSeat> bookingSeats = bookingSeatRepository.findByBookingId(booking.getId());
        List<Long> seatIds = bookingSeats.stream().map(bs -> bs.getSeat().getId()).collect(Collectors.toList());
        seatService.releaseSeats(seatIds);
        // Update flight available seats
        flightService.updateAvailableSeats(booking.getFlight().getId(), seatIds.size());
        bookingRepository.save(booking);
        logger.info("f::Cancel success for {} by {}::", booking.getFlight(), booking.getPassengerEmail());
    }

    private BookingResponse convertToBookingResponse(Booking booking) {
        BookingResponse response = new BookingResponse();
        response.setId(booking.getId());
        response.setBookingReference(booking.getBookingReference());
        response.setPassengerName(booking.getPassengerName());
        response.setPassengerEmail(booking.getPassengerEmail());
        response.setPassengerPhone(booking.getPassengerPhone());
        response.setTotalAmount(booking.getTotalAmount());
        response.setStatus(booking.getStatus());
        response.setFlightNumber(booking.getFlight().getFlightNumber());
        response.setAirline(booking.getFlight().getAirline());
        response.setDepartureTime(booking.getFlight().getDepartureTime());
        response.setArrivalTime(booking.getFlight().getArrivalTime());
        response.setCreatedAt(booking.getCreatedAt());

        // Query the seat numbers
        List<BookingSeat> bookingSeats = bookingSeatRepository.findByBookingId(booking.getId());
        List<String> seatNumbers = bookingSeats.stream().map(bs -> bs.getSeat().getSeatNumber()).collect(Collectors.toList());
        response.setSeatNumbers(seatNumbers);

        // Add payment status
        Optional<Payment> paymentOpt = paymentService.getPaymentByBookingId(booking.getId());
        String paymentStatus = paymentOpt
                .map(p -> p.getPaymentStatus().toString())
                .orElse("PENDING");
        response.setPaymentStatus(paymentStatus);
        return response;
    }

    private BigDecimal calculateTotalAmount(List<Long> seatIds, Flight flight) {
        BigDecimal total = BigDecimal.ZERO;

        for (Long seatId : seatIds) {
            Optional<Seat> seatOpt = seatService.getSeatById(seatId);
            if (seatOpt.isPresent()) {
                Seat seat = seatOpt.get();
                total = total.add(flight.getBasePrice());
                if (seat.getAdditionalFee() != null) {
                    total = total.add(seat.getAdditionalFee());
                }
            }
        }
        //Add tax - 12%
        BigDecimal taxes = total.multiply(new BigDecimal("0.12"));
        return total.add(taxes);
    }

    private String generateBookingReference() {
        return "FB" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
