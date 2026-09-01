package com.smartbooking.booking.controller;


import com.smartbooking.booking.dto.BookingRequest;
import com.smartbooking.booking.dto.BookingResponse;
import com.smartbooking.booking.entity.Booking;
import com.smartbooking.booking.service.BookingService;
import com.smartbooking.entity.User;
import com.smartbooking.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(
            BookingService bookingService) {

        this.bookingService = bookingService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingResponse createBooking(@Valid @RequestBody BookingRequest request, Authentication authentication){

        String email = authentication.getName();

        return bookingService.createBooking(request,email);

    }

    @GetMapping
    public List<BookingResponse> getMyBookings(Authentication authentication){

        String email = authentication.getName();

        return bookingService.getMyBookings(email);
    }


    @GetMapping("/{id}")
    public BookingResponse getMyBooking(
            @PathVariable Long id,
            Authentication authentication) {

        String email = authentication.getName();

        return bookingService.getMyBooking(id, email);
    }

    @PutMapping("/{id}")
    public BookingResponse updateBooking(@PathVariable Long id,
                                         @Valid @RequestBody BookingRequest request,
                                         Authentication authentication){
        String email = authentication.getName();

        return bookingService.updateBooking(id,request,email);
    }

    @PatchMapping("/{id}/cancel")
    public BookingResponse cancelBooking(
            @PathVariable Long id,
            Authentication authentication) {

        return bookingService.cancelBooking(
                id,
                authentication.getName()
        );
    }

}
