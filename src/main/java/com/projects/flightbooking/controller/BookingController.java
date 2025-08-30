package com.projects.flightbooking.controller;

import com.projects.flightbooking.dto.booking.BookingRequest;
import com.projects.flightbooking.dto.booking.BookingResponse;
import com.projects.flightbooking.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookings")
@Tag(name = "Ticket Booking Management", description = "APIs for booking related operations")
public class BookingController {

    @Autowired
    BookingService bookingService;

    @PostMapping
    @Operation(summary = "Book Ticket", description = "Book seat/s for a specific flight")
    public ResponseEntity<BookingResponse> createBooking(@RequestBody BookingRequest request) {
        try {
            BookingResponse booking = bookingService.createBooking(request);
            return ResponseEntity.ok(booking);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{bookingReference}")
    @Operation(summary = "Get Booking By ReferenceId", description = "Retrieve the booking info by the given referenceId")
    public ResponseEntity<BookingResponse> getBookingByReference(@PathVariable String bookingReference) {
        return bookingService.getBookingByReference(bookingReference)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    @Operation(summary = "Get Booking By Email Id", description = "Retrieve the booking info by the given email id")
    public ResponseEntity<List<BookingResponse>> getBookingsByEmail(@PathVariable String email) {
        List<BookingResponse> bookings = bookingService.getBookingsByEmail(email);
        return ResponseEntity.ok(bookings);
    }

    @DeleteMapping("/{bookingReference}")
    @Operation(summary = "Cancel Ticket", description = "Cancel the booking by the given referenceId")
    public ResponseEntity<Void> cancelBooking(@PathVariable String bookingReference) {
        try {
            bookingService.cancelBooking(bookingReference);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
