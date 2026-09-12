package com.smartbooking.booking.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.smartbooking.booking.dto.RoomRequest;
import com.smartbooking.booking.dto.RoomResponse;
import com.smartbooking.booking.entity.RoomStatus;
import com.smartbooking.booking.service.RoomService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public RoomResponse createRoom(
            @Valid @RequestBody RoomRequest request) {

        return roomService.createRoom(request);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<RoomResponse> getAllRooms() {

        return roomService.getAllRooms();
    }

    @GetMapping("/{roomId}")
    @PreAuthorize("hasRole('ADMIN')")
    public RoomResponse getRoom(@PathVariable Long roomId) {

        return roomService.getRoom(roomId);
    }

    @PatchMapping("/{roomId}/status")
@PreAuthorize("hasRole('ADMIN')")
public RoomResponse updateRoomStatus(
        @PathVariable Long roomId,
        @RequestParam RoomStatus status) {

    return roomService.updateRoomStatus(roomId, status);
}

@GetMapping("/available")
@PreAuthorize("hasRole('CUSTOMER')")
public List<RoomResponse> getAvailableRooms(
        @RequestParam LocalDate checkInDate,
        @RequestParam LocalDate checkOutDate,
        @RequestParam Integer guests) {

    return roomService.getAvailableRooms(
            checkInDate,
            checkOutDate,
            guests
    );
}

@PutMapping("/{roomId}")
@PreAuthorize("hasRole('ADMIN')")
public RoomResponse updateRoom(
        @PathVariable Long roomId,
        @Valid @RequestBody RoomRequest request) {

    return roomService.updateRoom(roomId, request);
}

}