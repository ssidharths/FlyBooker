package com.projects.flightbooking.repository;

import com.projects.flightbooking.entity.Seat;
import com.projects.flightbooking.entity.enums.SeatStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {

    List<Seat> findByFlightIdOrderBySeatNumber(Long flightId);

    @Query("SELECT s FROM Seat s WHERE s.flight.id = :flightId AND s.status = :status")
    List<Seat> findByFlightIdAndStatus(@Param("flightId") Long flightId,
                                       @Param("status") SeatStatus status);

    List<Seat> findBySeatNumberAndFlightId(String seatNumber, Long flightId);

    //Pessimistic lock implementation
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM Seat s WHERE s.id = :seatId")
    Optional<Seat> findByIdWithLock(@Param("seatId") Long seatId);
}
