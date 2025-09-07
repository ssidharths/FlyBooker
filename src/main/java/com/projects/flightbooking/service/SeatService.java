package com.projects.flightbooking.service;

import com.projects.flightbooking.dto.seats.SeatResponse;
import com.projects.flightbooking.entity.Seat;
import com.projects.flightbooking.entity.enums.SeatStatus;
import com.projects.flightbooking.repository.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SeatService {
    @Autowired
    private SeatRepository seatRepository;

    public List<SeatResponse> getSeatsForFlight(Long flightId) {
        List<Seat> seats = seatRepository.findByFlightIdOrderBySeatNumber(flightId);
        return seats.stream()
                .map(this::convertToSeatResponse)
                .collect(Collectors.toList());
    }

    public List<SeatResponse> getAvailableSeats(Long flightId) {
        List<Seat> seats = seatRepository.findByFlightIdAndStatus(flightId, SeatStatus.AVAILABLE);
        return seats.stream()
                .map(this::convertToSeatResponse)
                .collect(Collectors.toList());
    }

    public Optional<Seat> getSeatById(Long id) {
        return seatRepository.findById(id);
    }


    public boolean isSeatAvailable(Long seatId) {
        Optional<Seat> seat = seatRepository.findByIdWithLock(seatId);
        return seat.isPresent() && seat.get().getStatus() == SeatStatus.AVAILABLE;
    }

    public void reserveSeats(List<Long> seatIds) {
        List<Seat> seats = seatRepository.findAllById(seatIds);
        seats.forEach(seat -> seat.setStatus(SeatStatus.OCCUPIED));
        seatRepository.saveAll(seats);
    }

    public void releaseSeats(List<Long> seatIds) {
        List<Seat> seats = seatRepository.findAllById(seatIds);
        seats.forEach(seat -> seat.setStatus(SeatStatus.AVAILABLE));
        seatRepository.saveAll(seats);
    }

    private SeatResponse convertToSeatResponse(Seat seat) {
        return new SeatResponse(
                seat.getId(),
                seat.getSeatNumber(),
                seat.getSeatClass(),
                seat.getStatus(),
                seat.getAdditionalFee()
        );
    }
}
