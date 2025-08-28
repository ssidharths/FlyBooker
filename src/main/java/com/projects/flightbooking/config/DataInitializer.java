package com.projects.flightbooking.config;


import com.projects.flightbooking.entity.Flight;
import com.projects.flightbooking.repository.FlightRepository;
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

    @Override
    public void run(String ...args) throws Exception{
        initializeFlights();
    }

    public void initializeFlights(){
        if (flightRepository.count()==0){
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

            // Save flights
            flightRepository.saveAll(flights);
            System.out.println("SAVED ALL FLIGHTS");
        }
    }
}
