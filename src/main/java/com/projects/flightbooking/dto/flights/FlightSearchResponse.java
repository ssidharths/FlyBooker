package com.projects.flightbooking.dto.flights;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@Data
public class FlightSearchResponse {
    private Long id;
    private String flightNumber;
    private String airline;
    private String aircraftType;
    private String originAirport;
    private String destinationAirport;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private BigDecimal price;
    private Integer availableSeats;
    private String duration;

    public FlightSearchResponse(Long id, String flightNumber, String airline, String aircraftType,
                                String originAirport, String destinationAirport, LocalDateTime departureTime,
                                LocalDateTime arrivalTime, BigDecimal price, Integer availableSeats) {
        this.id = id;
        this.flightNumber = flightNumber;
        this.airline = airline;
        this.aircraftType = aircraftType;
        this.originAirport = originAirport;
        this.destinationAirport = destinationAirport;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.price = price;
        this.availableSeats = availableSeats;
        this.duration = calculateDuration(departureTime, arrivalTime);
    }

    private String calculateDuration(LocalDateTime departure, LocalDateTime arrival) {
        long minutes = java.time.Duration.between(departure, arrival).toMinutes();
        long hours = minutes / 60;
        long remainingMinutes = minutes % 60;
        return String.format("%dh %dm", hours, remainingMinutes);
    }
}