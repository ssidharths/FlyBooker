package com.projects.flightbooking.service;

import com.projects.flightbooking.entity.enums.SeatClass;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class DynamicPricingService {

    public BigDecimal calculatePrice(BigDecimal basePrice, Integer availableSeats,
                                     Integer totalSeats, SeatClass seatClass) {

        // Calculate occupancy rate
        double occupancyRate = (double) (totalSeats - availableSeats) / totalSeats;

        // Dynamic pricing multiplier based on occupancy
        double demandMultiplier = 1.0;
        if (occupancyRate > 0.8) {
            demandMultiplier = 1.3; // High demand
        } else if (occupancyRate > 0.6) {
            demandMultiplier = 1.15; // Medium demand
        } else if (occupancyRate > 0.4) {
            demandMultiplier = 1.05; // Low-medium demand
        }

        // Class multipliers
        double classMultiplier = getClassMultiplier(seatClass);

        // Applying all the multipliers to the final price
        BigDecimal finalPrice = basePrice
                .multiply(BigDecimal.valueOf(demandMultiplier))
                .multiply(BigDecimal.valueOf(classMultiplier));

        return finalPrice.setScale(2, RoundingMode.HALF_UP);
    }

    private double getClassMultiplier(SeatClass seatClass) {
        return switch (seatClass) {
            case PREMIUM_ECONOMY -> 1.5;
            case BUSINESS -> 2.5;
            case FIRST -> 4.0;
            default -> 1.0;
        };
    }
}