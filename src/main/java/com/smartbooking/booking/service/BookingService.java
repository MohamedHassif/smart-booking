package com.smartbooking.booking.service;


import com.smartbooking.booking.dto.BookingRequest;
import com.smartbooking.booking.dto.BookingResponse;
import com.smartbooking.booking.entity.Booking;
import com.smartbooking.booking.entity.BookingStatus;
import com.smartbooking.booking.exception.BookingNotFoundException;
import com.smartbooking.booking.exception.BookingOperationException;
import com.smartbooking.booking.repository.BookingRepository;
import com.smartbooking.booking.specification.BookingSpecification;
import com.smartbooking.entity.User;
import com.smartbooking.exception.UserNotFoundException;
import com.smartbooking.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;

    public BookingService(BookingRepository bookingRepository,UserRepository userRepository)
    {
        this.bookingRepository=bookingRepository;
        this.userRepository=userRepository;
    }

    @Transactional
    public BookingResponse createBooking(BookingRequest request, String email){

        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new RuntimeException("User not found"));

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

    @Transactional(readOnly = true)
    public List<BookingResponse> getMyBookings(String email){

        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new RuntimeException("User not found"));

        List<Booking> bookings = bookingRepository.findByUserId(user.getId());

        return bookings.stream()
                .map(this::mapToBookingResponse)
                .toList();
    }

    private BookingResponse mapToBookingResponse(Booking booking) {

        return new BookingResponse(
                booking.getId(),
                booking.getBookingDate(),
                booking.getStatus(),
                booking.getNumberOfGuests(),
                booking.getCreatedAt(),
                booking.getUser().getId()
        );
    }


    @Transactional(readOnly = true)
    public BookingResponse getMyBooking(Long bookingId, String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new UserNotFoundException("User not found"));

        Booking booking = bookingRepository
                .findByIdAndUserId(bookingId, user.getId())
                .orElseThrow(() ->
                        new BookingNotFoundException("Booking not found"));

        return mapToBookingResponse(booking);
    }

    public BookingResponse updateBooking(Long bookingId,BookingRequest request,String email){

        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new UserNotFoundException("User not found!"));

        Booking booking = bookingRepository.findByIdAndUserId(bookingId,user.getId())
                .orElseThrow(()-> new BookingNotFoundException("Booking not found !"));

        if(booking.getStatus() != BookingStatus.PENDING){
            throw new BookingOperationException("Only PENDING booking can be updated");
        }

        booking.setBookingDate(request.getBookingDate());
        booking.setNumberOfGuests(request.getNumberOfGuest());

        Booking updatedBooking = bookingRepository.save(booking);


        return mapToBookingResponse(updatedBooking);
    }

    @Transactional
    public BookingResponse cancelBooking(
            Long bookingId,
            String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found"));

        Booking booking = bookingRepository
                .findByIdAndUserId(bookingId, user.getId())
                .orElseThrow(() ->
                        new BookingNotFoundException("Booking not found"));

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new BookingOperationException(
                    "Booking is already cancelled"
            );
        }

        booking.setStatus(BookingStatus.CANCELLED);

        Booking updatedBooking = bookingRepository.save(booking);

        return mapToBookingResponse(updatedBooking);
    }
//    @Transactional(readOnly = true)
//    public Page<BookingResponse> getAllBookings(Pageable pageable){
//
//        return bookingRepository
//                .findAll(pageable)
//                .map(this::mapToBookingResponse);
//    }

    public BookingResponse confirmBooking(Long bookingId){

        Booking booking = bookingRepository.findById(bookingId).orElseThrow(()-> new BookingNotFoundException("Booking not found"));

        if(booking.getStatus() != BookingStatus.PENDING){
            throw new BookingOperationException("Only pending booking can be CONFIRMED");
        }

        booking.setStatus(BookingStatus.CONFIRMED);

        Booking updateBooking = bookingRepository.save(booking);

        return mapToBookingResponse(updateBooking);
    }

    @Transactional(readOnly = true)
    public Page<BookingResponse> searchBookings(
            BookingStatus status,
            LocalDate fromDate,
            LocalDate toDate,
            Pageable pageable) {

        Specification<Booking> specification =
                Specification.allOf(
                        BookingSpecification.hasStatus(status),
                        BookingSpecification.fromDate(fromDate),
                        BookingSpecification.toDate(toDate)
                );

        return bookingRepository
                .findAll(specification, pageable)
                .map(this::mapToBookingResponse);
    }

}
