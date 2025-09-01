package com.projects.flightbooking.entity;

import com.projects.flightbooking.entity.enums.BookingStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String bookingReference;
    @Column(nullable=false)
    private String passengerName;
    @Column(nullable = false)
    private String passengerEmail;
    @Column(nullable = false)
    private String passengerPhone;
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;
    @Enumerated(EnumType.STRING)
    private BookingStatus status;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="flight_id", nullable = false)
    private Flight flight;
    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<BookingSeat> bookingSeats;
    @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL)
    private Payment payment;
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;

    public Booking(String bookingReference, String passengerName, String passengerEmail, String passengerPhone, BigDecimal totalAmount, Flight flight) {
        this.bookingReference = bookingReference;
        this.passengerName = passengerName;
        this.passengerEmail = passengerEmail;
        this.passengerPhone = passengerPhone;
        this.totalAmount = totalAmount;
        this.flight = flight;
    }
}
