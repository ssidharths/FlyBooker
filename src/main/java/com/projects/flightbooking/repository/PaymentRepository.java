package com.projects.flightbooking.repository;

import com.projects.flightbooking.entity.Payment;
import com.projects.flightbooking.entity.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByTransactionId(String transactionId);
    List<Payment> findByPaymentStatus(PaymentStatus status);
    Optional<Payment> findByBookingId(Long bookingId);

    @Query("SELECT p FROM Payment p WHERE p.paymentStatus = :status AND p.createdAt < :cutoffTime")
    List<Payment> findExpiredPayments(@Param("status") PaymentStatus status,
                                      @Param("cutoffTime") java.time.LocalDateTime cutoffTime);
}
