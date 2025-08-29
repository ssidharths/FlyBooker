package com.projects.flightbooking.dto.booking;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class BookingRequest {
    private Long flightId;
    private String passengerName;
    private String passengerEmail;
    private String passengerPhone;
    private List<Long> seatIds;
    private String paymentMethod;
}
