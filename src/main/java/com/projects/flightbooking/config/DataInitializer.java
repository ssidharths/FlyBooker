package com.projects.flightbooking.config;


import com.projects.flightbooking.entity.Flight;
import com.projects.flightbooking.entity.Seat;
import com.projects.flightbooking.entity.enums.SeatClass;
import com.projects.flightbooking.entity.enums.SeatStatus;
import com.projects.flightbooking.repository.FlightRepository;
import com.projects.flightbooking.repository.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Override
    public void run(String... args) throws Exception {
        initializeFlights();
    }

    public void initializeFlights() {
        if (flightRepository.count() == 0) {
            // Starting out fresh. Create some flights manually
            // Please note this is for testing purposes only

            Flight flight1 = new Flight(
                    "ADY131",
                    "Air Arabia",
                    "Airbus A320",
                    "AUH",
                    "TRV",
                    LocalDateTime.now().plusDays(1).withHour(8).withMinute(30),
                    LocalDateTime.now().plusDays(1).withHour(11).withMinute(45),
                    new BigDecimal("63000.00"),
                    90
            );
            Flight flight2 = new Flight(
                    "IGO6216",
                    "IndiGo",
                    "Airbus A20N",
                    "MAA",
                    "TRV",
                    LocalDateTime.now().plusDays(1).withHour(14).withMinute(20),
                    LocalDateTime.now().plusDays(1).withHour(17).withMinute(35),
                    new BigDecimal(3500),
                    90
            );
            Flight flight3 = new Flight(
                    "UAE531",
                    "Emirates",
                    "Boeing B77L",
                    "COK",
                    "DXB",
                    LocalDateTime.now().plusDays(1).withHour(19).withMinute(15),
                    LocalDateTime.now().plusDays(1).withHour(22).withMinute(30),
                    new BigDecimal(68000),
                    90
            );
            Flight flight4 = new Flight(
                    "OMA223",
                    "Oman Air",
                    "Boeing B738",
                    "MCT",
                    "COK",
                    LocalDateTime.now().plusDays(2).withHour(9).withMinute(0),
                    LocalDateTime.now().plusDays(2).withHour(17).withMinute(30),
                    new BigDecimal(68000),
                    90
            );
            Flight flight5 = new Flight(
                    "AXB744",
                    "Air India Express",
                    "Boeing B738",
                    "SHJ",
                    "CNN",
                    LocalDateTime.now().plusDays(3).withHour(12).withMinute(15),
                    LocalDateTime.now().plusDays(3).withHour(15).withMinute(45),
                    new BigDecimal(68000),
                    90
            );
            List<Flight> flights = new ArrayList<>(
                    Arrays.asList(flight1, flight2, flight3, flight4, flight5)
            );
            // Save flights and capture returned persisted flights with IDs
            List<Flight> savedFlights = flightRepository.saveAll(flights);

            // Initialize seats for each saved flight
            for (Flight savedFlight : savedFlights) {
                initializeSeatsForFlight(savedFlight);
            }
            System.out.println("SAVED ALL FLIGHTS WITH SEATS");
        }
    }

    private void initializeSeatsForFlight(Flight flight) {
        List<Seat> seats = new ArrayList<>();
        for (int row = 1; row <= 15; row++) {
            char[] seatLetters = {'A', 'B', 'C', 'D', 'E', 'F'};
            for (char seatLetter : seatLetters) {
                String seatNumber = row + String.valueOf(seatLetter);
                SeatClass seatClass;
                BigDecimal additionalFee = BigDecimal.ZERO;
                // Assign seat class by row range
                if (row <= 2) {
                    seatClass = SeatClass.FIRST;
                    additionalFee = new BigDecimal("500.00"); // Example
                } else if (row <= 5) {
                    seatClass = SeatClass.BUSINESS;
                    additionalFee = new BigDecimal("200.00");
                } else if (row <= 8) {
                    seatClass = SeatClass.PREMIUM_ECONOMY;
                    additionalFee = new BigDecimal("50.00");
                } else {
                    seatClass = SeatClass.ECONOMY;
                    additionalFee = BigDecimal.ZERO;
                }
                Seat seat = new Seat(seatNumber, seatClass, additionalFee, flight);
                seat.setStatus(SeatStatus.AVAILABLE);
                seats.add(seat);
            }
        }

        // Mark some seats as occupied for demo
        String[] occupiedSeats = {"1A", "1B", "3C", "5D", "7F", "9A", "11E"};
        for (Seat seat : seats) {
            for (String occ : occupiedSeats) {
                if (seat.getSeatNumber().equals(occ)) {
                    seat.setStatus(SeatStatus.OCCUPIED);
                    break;
                }
            }
        }
        seatRepository.saveAll(seats);

        long availableSeats = seats.stream()
                .filter(s -> s.getStatus() == SeatStatus.AVAILABLE)
                .count();
        flight.setAvailableSeats((int) availableSeats);
        flightRepository.save(flight);
    }

}