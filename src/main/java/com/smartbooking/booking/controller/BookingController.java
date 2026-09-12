package com.smartbooking.booking.controller;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.smartbooking.booking.dto.BookingRequest;
import com.smartbooking.booking.dto.BookingResponse;
import com.smartbooking.booking.service.BookingService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@SecurityRequirement(name = "bearerAuth")
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

    @GetMapping("/my")
    public Page<BookingResponse> getMyBookings(Authentication authentication, Pageable pageable){

        String email = authentication.getName();

        return bookingService.getMyBookings(email,pageable);
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

@PatchMapping("/{bookingId}/complete")
@PreAuthorize("hasRole('ADMIN')")
public BookingResponse completeBooking(
        @PathVariable Long bookingId) {

    return bookingService.completeBooking(bookingId);
}



}
