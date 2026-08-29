package com.smartbooking.booking.controller;


import com.smartbooking.booking.dto.BookingRequest;
import com.smartbooking.booking.dto.BookingResponse;
import com.smartbooking.booking.entity.Booking;
import com.smartbooking.booking.service.BookingService;
import com.smartbooking.entity.User;
import com.smartbooking.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final UserRepository userRepository;

    public BookingController(
            BookingService bookingService,
            UserRepository userRepository) {

        this.bookingService = bookingService;
        this.userRepository = userRepository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingResponse createBooking(@Valid @RequestBody BookingRequest request, Authentication authentication){

        String email = authentication.getName();

        User user = userRepository.findByEmail(email).orElseThrow(()-> new RuntimeException("User not found"));

        return bookingService.createBooking(request,user);

    }


}
