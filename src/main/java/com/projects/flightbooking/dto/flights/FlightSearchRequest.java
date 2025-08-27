package com.projects.flightbooking.dto.flights;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FlightSearchRequest {
    private String origin;
    private String destination;
    private LocalDate departureDate;
    private LocalDate returnDate;
    private Integer passengers;
    private String travelClass;
}
