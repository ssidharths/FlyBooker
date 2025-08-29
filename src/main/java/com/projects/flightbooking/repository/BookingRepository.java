package com.projects.flightbooking.repository;

import com.projects.flightbooking.entity.Booking;
import com.projects.flightbooking.entity.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    Optional<Booking> findByBookingReference(String bookingReference);

    List<Booking> findByPassengerEmailOrderByCreatedAtDesc(String passengerEmail);

    @Query("SELECT b from Booking b WHERE b.status = :status AND b.createdAt < :cutOffTime")
    List<Booking> findExpiredBookings(@Param("status") BookingStatus status,
                                      @Param("cutOffTime") LocalDateTime cutOffTime);

    @Query("SELECT COUNT(b) FROM Booking b WHERE b.flight.id = :flightId AND b.status = :status")
    Long countByFlightIdAndStatus(@Param("flightId") Long flightId,
                                  @Param("status") BookingStatus status);
}
