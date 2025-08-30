package com.projects.flightbooking.service;

import com.projects.flightbooking.entity.Booking;
import com.projects.flightbooking.entity.Payment;
import com.projects.flightbooking.entity.enums.PaymentMethod;
import com.projects.flightbooking.entity.enums.PaymentStatus;
import com.projects.flightbooking.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    public void createPayment(Booking booking, PaymentMethod paymentMethod) {
        String transactionId = generateTransactionId();
        Payment payment = new Payment(transactionId, booking.getTotalAmount(), paymentMethod, booking);
        paymentRepository.save(payment);
    }

    public Optional<Payment> getPaymentByTransactionId(String transactionId) {
        return paymentRepository.findByTransactionId(transactionId);
    }


    public Optional<Payment> getPaymentByBookingId(Long bookingId) {
        return paymentRepository.findByBookingId(bookingId);
    }

    public Payment processPayment(String transactionId) {
        Optional<Payment> paymentOpt = paymentRepository.findByTransactionId(transactionId);
        if (paymentOpt.isEmpty()) {
            throw new RuntimeException("Payment not found");
        }
        Payment payment = paymentOpt.get();
        //We are not implementing any payment processors here for the sake of simplicity.
        //It simply checks if the generated random number is less than 0.95
        //This mimics the 95% of the cases, payment getting successful and 5% failure
        boolean paymentSuccessful = simulatePaymentProcessing();
        if (paymentSuccessful) {
            payment.setPaymentStatus(PaymentStatus.COMPLETED);
        } else {
            payment.setPaymentStatus(PaymentStatus.FAILED);
        }
        return paymentRepository.save(payment);
    }

    private String generateTransactionId() {
        return "TXN" + UUID.randomUUID().toString().substring(0, 12).toUpperCase();
    }

    private boolean simulatePaymentProcessing() {
        // Simulate 95% success rate
        return Math.random() < 0.95;
    }
}
