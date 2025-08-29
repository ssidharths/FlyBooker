package com.projects.flightbooking.repository;

import com.projects.flightbooking.entity.Seat;
import com.projects.flightbooking.entity.enums.SeatStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {

    List<Seat> findByFlightIdOrderBySeatNumber(Long flightId);

    @Query("SELECT s FROM Seat s WHERE s.flight.id = :flightId AND s.status = :status")
    List<Seat> findByFlightIdAndStatus(@Param("flightId") Long flightId,
                                       @Param("status") SeatStatus status);

    List<Seat> findBySeatNumberAndFlightId(String seatNumber, Long flightId);
}
