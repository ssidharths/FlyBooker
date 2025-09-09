package com.projects.flightbooking.entity;

import com.projects.flightbooking.entity.enums.SeatClass;
import com.projects.flightbooking.entity.enums.SeatStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "seats")
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String seatNumber;
    @Enumerated(EnumType.STRING)
    private SeatClass seatClass;
    @Enumerated
    private SeatStatus status;
    @Column(precision = 10, scale = 2)
    private BigDecimal additionalFee;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "flight_id", nullable = false)
    private Flight flight;

    public Seat(String seatNumber, SeatClass seatClass, BigDecimal additionalFee, Flight flight) {
        this.seatNumber = seatNumber;
        this.seatClass = seatClass;
        this.additionalFee = additionalFee;
        this.flight = flight;
    }
}
