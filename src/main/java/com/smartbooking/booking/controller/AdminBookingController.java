package com.smartbooking.booking.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.smartbooking.booking.dto.BookingResponse;
import com.smartbooking.booking.entity.BookingSortFields;
import com.smartbooking.booking.entity.BookingStatus;
import com.smartbooking.booking.entity.BookingStatusHistory;
import com.smartbooking.booking.service.BookingService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@SecurityRequirement(name = "bearerAuth")
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
        @PathVariable Long id,
        Authentication authentication) {

    return bookingService.confirmBooking(
            id,
            authentication.getName()
    );
}

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Page<BookingResponse> searchBookings(
            @RequestParam(required = false) BookingStatus status,
            @RequestParam(required = false) LocalDate fromDate,
            @RequestParam(required = false) LocalDate toDate,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String email,
            Pageable pageable) {

        for(var order : pageable.getSort()){
            if(!BookingSortFields.ALLOWED_FIELDS.contains(order.getProperty())){
                throw new IllegalArgumentException( "Sorting by '" + order.getProperty() + "' is not allowed");
            }
        }

        return bookingService.searchBookings(
                status,
                fromDate,
                toDate,
                userId,
                email,
                pageable
        );
    }

    @GetMapping("/{bookingId}/history")
@PreAuthorize("hasRole('ADMIN')")
public List<BookingStatusHistory> getBookingHistory(
        @PathVariable Long bookingId) {

    return bookingService.getBookingHistory(bookingId);
}


}
