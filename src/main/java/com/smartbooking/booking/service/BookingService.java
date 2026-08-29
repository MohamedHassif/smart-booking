package com.smartbooking.booking.service;


import com.smartbooking.booking.dto.BookingRequest;
import com.smartbooking.booking.dto.BookingResponse;
import com.smartbooking.booking.entity.Booking;
import com.smartbooking.booking.entity.BookingStatus;
import com.smartbooking.booking.repository.BookingRepository;
import com.smartbooking.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;

    public BookingService(BookingRepository bookingRepository){
        this.bookingRepository=bookingRepository;
    }

    @Transactional
    public BookingResponse createBooking(BookingRequest request, User user){

        Booking booking = new Booking();

        booking.setBookingDate(request.getBookingDate());
        booking.setNumberOfGuests(request.getNumberOfGuest());
        booking.setStatus(BookingStatus.PENDING);
        booking.setCreatedAt(LocalDateTime.now());
        booking.setUser(user);

        Booking savedBooking = bookingRepository.save(booking);

        return new BookingResponse(
                savedBooking.getId(),
                savedBooking.getBookingDate(),
                savedBooking.getStatus(),
                savedBooking.getNumberOfGuests(),
                savedBooking.getCreatedAt(),
                savedBooking.getUser().getId()
        );

    }




}
