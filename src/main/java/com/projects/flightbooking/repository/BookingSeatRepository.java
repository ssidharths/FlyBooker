package com.projects.flightbooking.repository;

import com.projects.flightbooking.entity.BookingSeat;
import com.projects.flightbooking.entity.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingSeatRepository extends JpaRepository<BookingSeat, Long> {
    List<BookingSeat> findByBookingId(Long bookingId);
    List<BookingSeat> findBySeatId(Long seatId);

    @Query("SELECT CASE WHEN COUNT(bs) > 0 THEN true ELSE false END FROM BookingSeat bs WHERE bs.seat.id = :seatId AND bs.booking.status IN :statuses")
    boolean existsBySeatIdAndBookingStatusIn(@Param("seatId") Long seatId, @Param("statuses") List<BookingStatus> statuses);

    // Get active bookings for a seat (useful for debugging)
    @Query("SELECT bs FROM BookingSeat bs WHERE bs.seat.id = :seatId AND bs.booking.status IN :statuses")
    List<BookingSeat> findBySeatIdAndBookingStatusIn(@Param("seatId") Long seatId, @Param("statuses") List<BookingStatus> statuses);

    // Clean delete method for cancellations
    @Modifying
    @Query("DELETE FROM BookingSeat bs WHERE bs.booking.id = :bookingId")
    void deleteByBookingId(@Param("bookingId") Long bookingId);
}

