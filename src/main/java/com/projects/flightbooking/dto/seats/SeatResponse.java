package com.projects.flightbooking.dto.seats;

import com.projects.flightbooking.entity.enums.SeatClass;
import com.projects.flightbooking.entity.enums.SeatStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeatResponse {
    private Long id;
    private String seatNumber;
    private SeatClass seatClass;
    private SeatStatus status;
    private BigDecimal additionalFee;
}
