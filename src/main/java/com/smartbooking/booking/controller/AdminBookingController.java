package com.smartbooking.booking.controller;

import com.smartbooking.booking.dto.BookingResponse;
import com.smartbooking.booking.entity.BookingStatus;
import com.smartbooking.booking.service.BookingService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/admin/bookings")
public class AdminBookingController {

    private final BookingService bookingService;

    public AdminBookingController(
            BookingService bookingService) {

        this.bookingService = bookingService;
    }

//    @GetMapping
//    @PreAuthorize("hasRole('ADMIN')")
//    public Page<BookingResponse> getAllBookings(Pageable pageable){
//
//        return bookingService.getAllBookings(pageable);
//
//    }



    @PatchMapping("/{id}/confirm")
    @PreAuthorize("hasRole('ADMIN')")
    public BookingResponse confirmBooking(
            @PathVariable Long id) {

        return bookingService.confirmBooking(id);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Page<BookingResponse> searchBookings(
            @RequestParam(required = false) BookingStatus status,
            @RequestParam(required = false) LocalDate fromDate,
            @RequestParam(required = false) LocalDate toDate,
            Pageable pageable) {

        return bookingService.searchBookings(
                status,
                fromDate,
                toDate,
                pageable
        );
    }

}
