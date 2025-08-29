package com.projects.flightbooking.controller;

import com.projects.flightbooking.dto.seats.SeatResponse;
import com.projects.flightbooking.service.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/v1/seats")
@Tag(name = "Seat Management", description = "APIs for seat selection and management")
public class SeatController {

    @Autowired
    SeatService seatService;

    @GetMapping("/flight/{flightId}")
    @Operation(summary = "Get seats for flight", description = "Retrieve all seats for a specific flight")
    public ResponseEntity<List<SeatResponse>> getSeatsForFlight(@PathVariable Long flightId) {
        List<SeatResponse> seats = seatService.getSeatsForFlight(flightId);
        return ResponseEntity.ok(seats);
    }

    @GetMapping("/flight/{flightId}/available")
    @Operation(summary = "Get available seats", description = "Retrieve available seats for a specific flight")
    public ResponseEntity<List<SeatResponse>> getAvailableSeats(@PathVariable Long flightId){
        List<SeatResponse> seats = seatService.getAvailableSeats(flightId);
        return ResponseEntity.ok(seats);
    }

    @GetMapping("/{seatId}/availability")
    @Operation(summary = "Check seat availability", description = "Check if a specific seat is available")
    public ResponseEntity<Boolean> checkSeatAvailability(@PathVariable Long seatId) {
        boolean available = seatService.isSeatAvailable(seatId);
        return ResponseEntity.ok(available);
    }

}
