package com.projects.flightbooking.repository;

import com.projects.flightbooking.entity.Flight;
import com.projects.flightbooking.entity.enums.FlightStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface FlightRepository extends JpaRepository<Flight,Long> {
    @Query("SELECT f from Flight f WHERE f.originAirport =:origin "+
            "AND f.destinationAirport = :destination " +
            "AND f.departureTime >= :startOfDay " +
            "AND f.departureTime < :startOfNextDay " +
            "AND f.status = :status "+
            "AND f.availableSeats > 0"+
            "ORDER BY f.departureTime")
    List<Flight> findAvailableFlights(@Param("origin") String origin,
                                      @Param("destination") String destination,
                                      @Param("startOfDay")LocalDateTime startOfDay,
                                      @Param("startOfNextDay")LocalDateTime startOfNextDay,
                                      @Param("status")FlightStatus status
                                      );

    Optional<Flight> findByFlightNumber(String flightNumber);

    @Query("SELECT f FROM Flight f WHERE f.departureTime BETWEEN :startTime AND :endTime")
    List<Flight> findFlightsByTimeRange(@Param("startTime")LocalDateTime startTime,
                                        @Param("endTime") LocalDateTime endTime);
}