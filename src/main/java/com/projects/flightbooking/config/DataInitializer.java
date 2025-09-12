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
    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

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
            //Define your base 20 flights (without departure/arrival times)
            List<Flight> baseFlights = Arrays.asList(
                    new Flight("ADY131", "Air Arabia", "Airbus A320", "AUH", "TRV", null, null, new BigDecimal("63000.00"), 90),
                    new Flight("IGO6216", "IndiGo", "Airbus A20N", "MAA", "TRV", null, null, new BigDecimal("3500"), 90),
                    new Flight("UAE531", "Emirates", "Boeing B77L", "COK", "DXB", null, null, new BigDecimal("68000"), 90),
                    new Flight("OMA223", "Oman Air", "Boeing B738", "MCT", "COK", null, null, new BigDecimal("68000"), 90),
                    new Flight("AXB744", "Air India Express", "Boeing B738", "SHJ", "CNN", null, null, new BigDecimal("68000"), 90),
                    new Flight("QTR507", "Qatar Airways", "Airbus A359", "DOH", "DEL", null, null, new BigDecimal("72000.00"), 90),
                    new Flight("SIA471", "Singapore Airlines", "Boeing B789", "SIN", "MAA", null, null, new BigDecimal("54000.00"), 90),
                    new Flight("BA138", "British Airways", "Boeing B789", "LHR", "BLR", null, null, new BigDecimal("89000.00"), 90),
                    new Flight("AIC102", "Air India", "Boeing B77W", "JFK", "DEL", null, null, new BigDecimal("102000.00"), 90),
                    new Flight("ETD565", "Etihad Airways", "Boeing B789", "AUH", "COK", null, null, new BigDecimal("65000.00"), 90),
                    new Flight("IGO341", "IndiGo", "Airbus A320", "BLR", "CCU", null, null, new BigDecimal("4800.00"), 90),
                    new Flight("AIC673", "Air India", "Airbus A321", "DEL", "MAA", null, null, new BigDecimal("7200.00"), 90),
                    new Flight("SIA233", "Singapore Airlines", "Airbus A359", "SIN", "COK", null, null, new BigDecimal("56000.00"), 90),
                    new Flight("CPA712", "Cathay Pacific", "Boeing B77W", "HKG", "HYD", null, null, new BigDecimal("64000.00"), 90),
                    new Flight("AF225", "Air France", "Boeing B789", "CDG", "DEL", null, null, new BigDecimal("95000.00"), 90),
                    new Flight("LH763", "Lufthansa", "Airbus A388", "FRA", "BOM", null, null, new BigDecimal("99000.00"), 90),
                    new Flight("QFA2", "Qantas", "Boeing B789", "SYD", "LHR", null, null, new BigDecimal("132000.00"), 90),
                    new Flight("UAL870", "United Airlines", "Boeing B789", "SFO", "DEL", null, null, new BigDecimal("118000.00"), 90),
                    new Flight("AIC174", "Air India", "Boeing B788", "CCU", "LHR", null, null, new BigDecimal("87000.00"), 90),
                    new Flight("DL40", "Delta Airlines", "Airbus A333", "JFK", "AMS", null, null, new BigDecimal("92000.00"), 90)
            );

            //Generate copies of flights for the next 60 days
            List<Flight> allFlights = new ArrayList<>();
            for (Flight base : baseFlights) {
                for (int day = 1; day <= 60; day++) {
                    LocalDateTime departure = LocalDateTime.now().plusDays(day).withHour(10).withMinute(0);
                    LocalDateTime arrival = departure.plusHours(3); // Dummy 3 hr journey

                    Flight flightCopy = new Flight(
                            base.getFlightNumber(),
                            base.getAirline(),
                            base.getAircraftType(),
                            base.getOriginAirport(),
                            base.getDestinationAirport(),
                            departure,
                            arrival,
                            base.getBasePrice(),
                            base.getTotalSeats()
                    );

                    allFlights.add(flightCopy);
                }
            }

            //Save flights & initialize seats
            List<Flight> savedFlights = flightRepository.saveAll(allFlights);
            for (Flight savedFlight : savedFlights) {
                initializeSeatsForFlight(savedFlight);
            }

            logger.info("--- ALL FLIGHTS INITIALIZED FOR NEXT 60 DAYS ---");
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

                if (row <= 2) {
                    seatClass = SeatClass.FIRST;
                    additionalFee = new BigDecimal("500.00");
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
