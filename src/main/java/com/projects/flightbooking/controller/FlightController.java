package com.projects.flightbooking.controller;

import com.projects.flightbooking.dto.flights.FlightSearchRequest;
import com.projects.flightbooking.dto.flights.FlightSearchResponse;
import com.projects.flightbooking.service.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/flights/")
public class FlightController {
    @Autowired
    private FlightService flightService;

    @PostMapping("/search")
    public ResponseEntity<List<FlightSearchResponse>> searchFlights(@RequestBody FlightSearchRequest request){
        List<FlightSearchResponse> flights = flightService.searchFlights(request);
        return ResponseEntity.ok(flights);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FlightSearchResponse> getFlightById(@PathVariable Long id){
        return flightService.getFlightById(id)
                .map(flight -> {
                    FlightSearchResponse response = new FlightSearchResponse(
                            flight.getId(),
                            flight.getFlightNumber(),
                            flight.getAirline(),
                            flight.getAircraftType(),
                            flight.getOriginAirport(),
                            flight.getDestinationAirport(),
                            flight.getDepartureTime(),
                            flight.getArrivalTime(),
                            flight.getBasePrice(),
                            flight.getAvailableSeats()
                    );
                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
