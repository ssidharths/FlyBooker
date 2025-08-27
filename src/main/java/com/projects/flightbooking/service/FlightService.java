package com.projects.flightbooking.service;

import com.projects.flightbooking.dto.flights.FlightSearchRequest;
import com.projects.flightbooking.dto.flights.FlightSearchResponse;
import com.projects.flightbooking.entity.Flight;
import com.projects.flightbooking.entity.enums.FlightStatus;
import com.projects.flightbooking.entity.enums.SeatClass;
import com.projects.flightbooking.repository.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FlightService {
    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private DynamicPricingService dynamicPricingService;

    public List<FlightSearchResponse> searchFlights(FlightSearchRequest request) {
        LocalDateTime departureDate = request.getDepartureDate().atStartOfDay();

        List<Flight> flights = flightRepository.findAvailableFlights(request.getOrigin(),
                request.getDestination(), departureDate, FlightStatus.SCHEDULED);

        return flights.stream().map(flight -> convertToFlightResponse(flight,
                request.getTravelClass())).collect(Collectors.toList());
    }

    public Optional<Flight> getFlightById(Long id) {
        return flightRepository.findById(id);
    }

    public Optional<Flight> getFlightByNumber(String flightNumber) {
        return flightRepository.findByFlightNumber(flightNumber);
    }

    public Flight saveFlight(Flight flight) {
        return flightRepository.save(flight);
    }

    public void updateAvailableSeats(Long flightId, int seatChange) {
        Optional<Flight> flightOpt = flightRepository.findById(flightId);
        if (flightOpt.isPresent()) {
            Flight flight = flightOpt.get();
            flight.setAvailableSeats(flight.getAvailableSeats() + seatChange);
            flightRepository.save(flight);
        }
    }

    private FlightSearchResponse convertToFlightResponse(Flight flight, String travelClass) {
        BigDecimal adjustedPrice = dynamicPricingService.calculatePrice(flight.getBasePrice(),
                flight.getAvailableSeats(), flight.getTotalSeats(), SeatClass.valueOf(travelClass.toUpperCase()));

        return new FlightSearchResponse(flight.getId(), flight.getFlightNumber(), flight.getAirline(),
                flight.getAircraftType(), flight.getOriginAirport(), flight.getDestinationAirport(),
                flight.getDepartureTime(), flight.getArrivalTime(), adjustedPrice, flight.getAvailableSeats());
    }
}
