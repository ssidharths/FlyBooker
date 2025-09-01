package com.projects.flightbooking.service;

import com.projects.flightbooking.entity.Booking;
import com.projects.flightbooking.entity.Payment;
import com.projects.flightbooking.entity.enums.PaymentMethod;
import com.projects.flightbooking.entity.enums.PaymentStatus;
import com.projects.flightbooking.repository.PaymentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class PaymentService {
    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

    @Autowired
    private PaymentRepository paymentRepository;

    public Payment createPayment(Booking booking, PaymentMethod method) {
        String transactionId = generateTransactionId();

        // Start with PENDING status
        Payment payment = new Payment(
                        transactionId,
                        booking.getTotalAmount(),
                        method,
                        booking,
                        PaymentStatus.PENDING
                );

        return paymentRepository.save(payment);
    }

    //Payment processing failure shouldn't roll back the entire booking transaction, hence the
    //separation transaction.We can also record the booking as CANCELLED even if payment fails.
    //REQUIRES_NEW propagation so payment processing doesn't affect the main booking transaction.
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Payment processPayment(String transactionId) {
        Optional<Payment> paymentOpt = paymentRepository.findByTransactionId(transactionId);
        if (paymentOpt.isEmpty()) {
            throw new RuntimeException("Payment not found");
        }
        Payment payment = paymentOpt.get();

        logger.info("Processing payment {} for amount {}", transactionId, payment.getAmount());

        // Simulate payment processing
        boolean paymentSuccessful = simulatePaymentProcessing();
        logger.info("Payment successful: {}", paymentSuccessful);

        if (paymentSuccessful) {
            payment.setPaymentStatus(PaymentStatus.COMPLETED);
            logger.info("Payment set to COMPLETED");
        } else {
            payment.setPaymentStatus(PaymentStatus.FAILED);
            logger.info("Payment set to FAILED");
        }

        Payment savedPayment = paymentRepository.save(payment);
        logger.info("Payment saved with status: {}", savedPayment.getPaymentStatus());

        return savedPayment;
    }

    public Optional<Payment> getPaymentByTransactionId(String transactionId) {
        return paymentRepository.findByTransactionId(transactionId);
    }


    public Optional<Payment> getPaymentByBookingId(Long bookingId) {
        return paymentRepository.findByBookingId(bookingId);
    }

    private String generateTransactionId() {
        return "TXN" + UUID.randomUUID().toString().substring(0, 12).toUpperCase();
    }

    private boolean simulatePaymentProcessing() {
        // Simulate 95% success rate
        return Math.random() < 0.95;
    }
}
