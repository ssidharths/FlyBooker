package com.projects.flightbooking.controller;

import com.projects.flightbooking.service.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/seats")
@Tag(name = "Seat Management", description = "APIs for seat selection and management")
public class SeatController {
    @Autowired
    SeatService seatService;

}
