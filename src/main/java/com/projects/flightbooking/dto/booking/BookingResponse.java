package com.projects.flightbooking.dto.booking;

import com.projects.flightbooking.entity.enums.BookingStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class BookingResponse {
    private Long id;
    private String bookingReference;
    private String passengerName;
    private String passengerEmail;
    private BigDecimal totalAmount;
    private BookingStatus status;
    private String flightNumber;
    private String airline;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private List<String> seatNumbers;
    private LocalDateTime createdAt;
    private String paymentStatus;
}
