package com.projects.flightbooking.config;


import com.projects.flightbooking.entity.Flight;
import com.projects.flightbooking.entity.Seat;
import com.projects.flightbooking.entity.enums.SeatClass;
import com.projects.flightbooking.entity.enums.SeatStatus;
import com.projects.flightbooking.repository.FlightRepository;
import com.projects.flightbooking.repository.SeatRepository;
import com.projects.flightbooking.service.BookingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(BookingService.class);

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
            // Please note this is for TESTING PURPOSES ONLY

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
            Flight flight6 = new Flight(
                    "QTR507",
                    "Qatar Airways",
                    "Airbus A359",
                    "DOH",
                    "DEL",
                    LocalDateTime.now().plusDays(2).withHour(2).withMinute(45),
                    LocalDateTime.now().plusDays(2).withHour(9).withMinute(55),
                    new BigDecimal("72000.00"),
                    90
            );

            Flight flight7 = new Flight(
                    "SIA471",
                    "Singapore Airlines",
                    "Boeing B789",
                    "SIN",
                    "MAA",
                    LocalDateTime.now().plusDays(1).withHour(23).withMinute(15),
                    LocalDateTime.now().plusDays(2).withHour(5).withMinute(40),
                    new BigDecimal("54000.00"),
                    90
            );

            Flight flight8 = new Flight(
                    "BA138",
                    "British Airways",
                    "Boeing B789",
                    "LHR",
                    "BLR",
                    LocalDateTime.now().plusDays(3).withHour(10).withMinute(45),
                    LocalDateTime.now().plusDays(3).withHour(23).withMinute(5),
                    new BigDecimal("89000.00"),
                    90
            );

            Flight flight9 = new Flight(
                    "AIC102",
                    "Air India",
                    "Boeing B77W",
                    "JFK",
                    "DEL",
                    LocalDateTime.now().plusDays(1).withHour(21).withMinute(30),
                    LocalDateTime.now().plusDays(2).withHour(20).withMinute(15),
                    new BigDecimal("102000.00"),
                    90
            );

            Flight flight10 = new Flight(
                    "ETD565",
                    "Etihad Airways",
                    "Boeing B789",
                    "AUH",
                    "COK",
                    LocalDateTime.now().plusDays(2).withHour(16).withMinute(25),
                    LocalDateTime.now().plusDays(2).withHour(22).withMinute(5),
                    new BigDecimal("65000.00"),
                    90
            );
            Flight flight11 = new Flight(
                    "IGO341",
                    "IndiGo",
                    "Airbus A320",
                    "BLR",
                    "CCU",
                    LocalDateTime.now().plusDays(1).withHour(6).withMinute(10),
                    LocalDateTime.now().plusDays(1).withHour(9).withMinute(0),
                    new BigDecimal("4800.00"),
                    90
            );

            Flight flight12 = new Flight(
                    "AIC673",
                    "Air India",
                    "Airbus A321",
                    "DEL",
                    "MAA",
                    LocalDateTime.now().plusDays(2).withHour(12).withMinute(45),
                    LocalDateTime.now().plusDays(2).withHour(15).withMinute(55),
                    new BigDecimal("7200.00"),
                    90
            );

            Flight flight13 = new Flight(
                    "SIA233",
                    "Singapore Airlines",
                    "Airbus A359",
                    "SIN",
                    "COK",
                    LocalDateTime.now().plusDays(3).withHour(9).withMinute(30),
                    LocalDateTime.now().plusDays(3).withHour(15).withMinute(45),
                    new BigDecimal("56000.00"),
                    90
            );

            Flight flight14 = new Flight(
                    "CPA712",
                    "Cathay Pacific",
                    "Boeing B77W",
                    "HKG",
                    "HYD",
                    LocalDateTime.now().plusDays(2).withHour(23).withMinute(20),
                    LocalDateTime.now().plusDays(3).withHour(4).withMinute(50),
                    new BigDecimal("64000.00"),
                    90
            );

            Flight flight15 = new Flight(
                    "AF225",
                    "Air France",
                    "Boeing B789",
                    "CDG",
                    "DEL",
                    LocalDateTime.now().plusDays(5).withHour(10).withMinute(15),
                    LocalDateTime.now().plusDays(5).withHour(23).withMinute(25),
                    new BigDecimal("95000.00"),
                    90
            );

            Flight flight16 = new Flight(
                    "LH763",
                    "Lufthansa",
                    "Airbus A388",
                    "FRA",
                    "BOM",
                    LocalDateTime.now().plusDays(4).withHour(13).withMinute(40),
                    LocalDateTime.now().plusDays(4).withHour(23).withMinute(55),
                    new BigDecimal("99000.00"),
                    90
            );

            Flight flight17 = new Flight(
                    "QFA2",
                    "Qantas",
                    "Boeing B789",
                    "SYD",
                    "LHR",
                    LocalDateTime.now().plusDays(6).withHour(18).withMinute(0),
                    LocalDateTime.now().plusDays(7).withHour(7).withMinute(30),
                    new BigDecimal("132000.00"),
                    90
            );

            Flight flight18 = new Flight(
                    "UAL870",
                    "United Airlines",
                    "Boeing B789",
                    "SFO",
                    "DEL",
                    LocalDateTime.now().plusDays(3).withHour(20).withMinute(25),
                    LocalDateTime.now().plusDays(4).withHour(2).withMinute(55),
                    new BigDecimal("118000.00"),
                    90
            );

            Flight flight19 = new Flight(
                    "AIC174",
                    "Air India",
                    "Boeing B788",
                    "CCU",
                    "LHR",
                    LocalDateTime.now().plusDays(7).withHour(2).withMinute(50),
                    LocalDateTime.now().plusDays(7).withHour(11).withMinute(30),
                    new BigDecimal("87000.00"),
                    90
            );

            Flight flight20 = new Flight(
                    "DL40",
                    "Delta Airlines",
                    "Airbus A333",
                    "JFK",
                    "AMS",
                    LocalDateTime.now().plusDays(2).withHour(22).withMinute(15),
                    LocalDateTime.now().plusDays(3).withHour(11).withMinute(10),
                    new BigDecimal("92000.00"),
                    90
            );

            List<Flight> flights = new ArrayList<>(
                    Arrays.asList(flight1, flight2, flight3, flight4, flight5, flight6, flight7, flight8, flight9,
                            flight10, flight11, flight12, flight13, flight14, flight15, flight16, flight17, flight18,
                            flight19, flight20)
            );
            // Save flights and capture returned persisted flights with IDs
            List<Flight> savedFlights = flightRepository.saveAll(flights);

            // Initialize seats for each saved flight
            for (Flight savedFlight : savedFlights) {
                initializeSeatsForFlight(savedFlight);
            }
            logger.info("--- ALL FLIGHTS INITIALIZED ---");
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