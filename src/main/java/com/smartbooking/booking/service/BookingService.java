package com.smartbooking.booking.service;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smartbooking.booking.dto.BookingRequest;
import com.smartbooking.booking.dto.BookingResponse;
import com.smartbooking.booking.entity.Booking;
import com.smartbooking.booking.entity.BookingStatus;
import com.smartbooking.booking.entity.BookingStatusHistory;
import com.smartbooking.booking.entity.Room;
import com.smartbooking.booking.entity.RoomStatus;
import com.smartbooking.booking.exception.BookingConflictException;
import com.smartbooking.booking.exception.BookingNotFoundException;
import com.smartbooking.booking.exception.BookingOperationException;
import com.smartbooking.booking.exception.RoomNotFoundException;
import com.smartbooking.booking.repository.BookingRepository;
import com.smartbooking.booking.repository.BookingStatusHistoryRepository;
import com.smartbooking.booking.repository.RoomRepository;
import com.smartbooking.booking.specification.BookingSpecification;
import com.smartbooking.entity.User;
import com.smartbooking.exception.UserNotFoundException;
import com.smartbooking.repository.UserRepository;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final BookingStatusHistoryRepository bookingStatusHistoryRepository;

public BookingService(
        BookingRepository bookingRepository,
        UserRepository userRepository,
        RoomRepository roomRepository,
        BookingStatusHistoryRepository bookingStatusHistoryRepository) {

    this.bookingRepository = bookingRepository;
    this.userRepository = userRepository;
    this.roomRepository = roomRepository;
    this.bookingStatusHistoryRepository = bookingStatusHistoryRepository;
}

@Transactional
public BookingResponse createBooking(
        BookingRequest request,
        String email) {

    // 1. Find user
    User user = userRepository.findByEmail(email)
            .orElseThrow(() ->
                    new UserNotFoundException("User not found"));

    // 2. Validate date range
    if (!request.getCheckOutDate()
            .isAfter(request.getCheckInDate())) {

        throw new IllegalArgumentException(
                "Check-out date must be after check-in date");
    }

    // 3. Find room
    Room room = roomRepository.findById(request.getRoomId())
            .orElseThrow(() ->
                    new RoomNotFoundException("Room not found"));

    // 4. Check room status
    if (room.getStatus() != RoomStatus.AVAILABLE) {

        throw new BookingOperationException(
                "Room is not available for booking");
    }

    // 5. Check room capacity
    if (request.getNumberOfGuest() > room.getCapacity()) {

        throw new BookingOperationException(
                "Number of guests exceeds room capacity");
    }

    // 6. Check overlapping bookings
boolean alreadyBooked =
        bookingRepository.existsOverlappingBooking(
                room.getId(),
                request.getCheckInDate(),
                request.getCheckOutDate(),
                List.of(
                        BookingStatus.PENDING,
                        BookingStatus.CONFIRMED
                )
        );

    if (alreadyBooked) {

        throw new BookingConflictException(
                "Room is already booked for the selected dates");
    }

    // 7. Create booking
    Booking booking = new Booking();

    booking.setCheckInDate(request.getCheckInDate());
    booking.setCheckOutDate(request.getCheckOutDate());
    booking.setNumberOfGuests(request.getNumberOfGuest());
    booking.setStatus(BookingStatus.PENDING);
    booking.setCreatedAt(LocalDateTime.now());
    booking.setUser(user);
    booking.setRoom(room);

    // 8. Save
    Booking savedBooking =
            bookingRepository.save(booking);

            saveStatusHistory(
        savedBooking,
        null,
        BookingStatus.PENDING,
        user
);

    return mapToBookingResponse(savedBooking);
}

@Transactional(readOnly = true)
public Page<BookingResponse> getMyBookings(
        String email,
        Pageable pageable) {

    User user = userRepository.findByEmail(email)
            .orElseThrow(() ->
                    new UserNotFoundException("User not found"));

    return bookingRepository
            .findByUserId(user.getId(), pageable)
            .map(this::mapToBookingResponse);
}

private BookingResponse mapToBookingResponse(Booking booking) {

    return new BookingResponse(
            booking.getId(),
            booking.getRoom().getId(),
            booking.getRoom().getRoomNumber(),
            booking.getCheckInDate(),
            booking.getCheckOutDate(),
            booking.getStatus(),
            booking.getNumberOfGuests(),
            booking.getCreatedAt(),
            booking.getUser().getId()
    );
}


@Transactional(readOnly = true)
public BookingResponse getMyBooking(
        Long bookingId,
        String email) {

    User user = userRepository.findByEmail(email)
            .orElseThrow(() ->
                    new UserNotFoundException("User not found"));

    Booking booking = bookingRepository
            .findByIdAndUserId(bookingId, user.getId())
            .orElseThrow(() ->
                    new BookingNotFoundException("Booking not found"));

    return mapToBookingResponse(booking);
}

@Transactional
public BookingResponse updateBooking(
        Long bookingId,
        BookingRequest request,
        String email) {

    User user = userRepository.findByEmail(email)
            .orElseThrow(() ->
                    new UserNotFoundException("User not found"));

    Booking booking = bookingRepository
            .findByIdAndUserId(bookingId, user.getId())
            .orElseThrow(() ->
                    new BookingNotFoundException("Booking not found"));

    // 1. Check booking status
    if (!booking.getStatus().canBeUpdated()) {
        throw new BookingOperationException(
                "Only PENDING booking can be updated");
    }

    // 2. Validate date range
    if (!request.getCheckOutDate()
            .isAfter(request.getCheckInDate())) {

        throw new IllegalArgumentException(
                "Check-out date must be after check-in date");
    }

    // 3. Find requested room
    Room room = roomRepository.findById(request.getRoomId())
            .orElseThrow(() ->
                    new RoomNotFoundException("Room not found"));

    // 4. Check room status
    if (room.getStatus() != RoomStatus.AVAILABLE) {
        throw new BookingOperationException(
                "Room is not available for booking");
    }

    // 5. Check room capacity
    if (request.getNumberOfGuest() > room.getCapacity()) {
        throw new BookingOperationException(
                "Number of guests exceeds room capacity");
    }

    // 6. Check overlapping bookings
boolean alreadyBooked =
        bookingRepository.existsOverlappingBookingForUpdate(
                bookingId,
                room.getId(),
                request.getCheckInDate(),
                request.getCheckOutDate(),
                List.of(
                        BookingStatus.PENDING,
                        BookingStatus.CONFIRMED
                )
        );

    if (alreadyBooked) {
        throw new BookingConflictException(
                "Room is already booked for the selected dates");
    }

    // 7. Update booking
    booking.setRoom(room);
    booking.setCheckInDate(request.getCheckInDate());
    booking.setCheckOutDate(request.getCheckOutDate());
    booking.setNumberOfGuests(request.getNumberOfGuest());

    Booking updatedBooking =
            bookingRepository.save(booking);

    return mapToBookingResponse(updatedBooking);
}

@Transactional
public BookingResponse cancelBooking(Long bookingId, String email) {

    User user = userRepository.findByEmail(email)
            .orElseThrow(() ->
                    new UserNotFoundException("User not found"));

    Booking booking = bookingRepository.findByIdAndUserId(
            bookingId,
            user.getId()
    ).orElseThrow(() ->
            new BookingNotFoundException("Booking not found"));

    if (!booking.getStatus().canBeCancelled()) {
        throw new BookingOperationException(
                "Booking cannot be cancelled"
        );
    }

    BookingStatus oldStatus = booking.getStatus();

    booking.setStatus(BookingStatus.CANCELLED);

    Booking cancelledBooking = bookingRepository.save(booking);

    saveStatusHistory(
            cancelledBooking,
            oldStatus,
            BookingStatus.CANCELLED,
            user
    );

    return mapToBookingResponse(cancelledBooking);
}
//    @Transactional(readOnly = true)
//    public Page<BookingResponse> getAllBookings(Pageable pageable){
//
//        return bookingRepository
//                .findAll(pageable)
//                .map(this::mapToBookingResponse);
//    }

@Transactional
public BookingResponse confirmBooking(Long bookingId, String email) {

    User admin = userRepository.findByEmail(email)
            .orElseThrow(() -> new UserNotFoundException("User not found"));

    Booking booking = bookingRepository.findById(bookingId)
            .orElseThrow(() ->
                    new BookingNotFoundException("Booking not found"));

    if (!booking.getStatus().canBeConfirmed()) {
        throw new BookingOperationException(
                "Only pending bookings can be confirmed"
        );
    }

    BookingStatus oldStatus = booking.getStatus();

    booking.setStatus(BookingStatus.CONFIRMED);

    Booking confirmedBooking = bookingRepository.save(booking);

    saveStatusHistory(
            confirmedBooking,
            oldStatus,
            BookingStatus.CONFIRMED,
            admin
    );

    return mapToBookingResponse(confirmedBooking);
}

    @Transactional(readOnly = true)
    public Page<BookingResponse> searchBookings(
            BookingStatus status,
            LocalDate fromDate,
            LocalDate toDate,
            Long userId,
            String email,
            Pageable pageable) {

        Specification<Booking> specification =
                Specification.allOf(
                        BookingSpecification.hasStatus(status),
                        BookingSpecification.fromDate(fromDate),
                        BookingSpecification.toDate(toDate),
                        BookingSpecification.hasUserId(userId),
                        BookingSpecification.hasEmail(email)
                );

        return bookingRepository
                .findAll(specification, pageable)
                .map(this::mapToBookingResponse);
    }


@Transactional
public BookingResponse completeBooking(Long bookingId) {

    Booking booking = bookingRepository.findById(bookingId)
            .orElseThrow(() ->
                    new BookingNotFoundException("Booking not found"));

    if (!booking.getStatus().canBeCompleted()) {
        throw new BookingOperationException(
                "Only confirmed bookings can be completed"
        );
    }

    BookingStatus oldStatus = booking.getStatus();

    booking.setStatus(BookingStatus.COMPLETED);

    Booking completedBooking = bookingRepository.save(booking);

    saveStatusHistory(
            completedBooking,
            oldStatus,
            BookingStatus.COMPLETED,
            null
    );

    return mapToBookingResponse(completedBooking);
}

private void saveStatusHistory(
        Booking booking,
        BookingStatus oldStatus,
        BookingStatus newStatus,
        User changedBy) {

    BookingStatusHistory history = new BookingStatusHistory();

    history.setBooking(booking);
    history.setOldStatus(oldStatus);
    history.setNewStatus(newStatus);
    history.setChangedBy(changedBy);
    history.setChangedAt(LocalDateTime.now());

    bookingStatusHistoryRepository.save(history);
}

public List<BookingStatusHistory> getBookingHistory(Long bookingId) {

    if (!bookingRepository.existsById(bookingId)) {
        throw new BookingNotFoundException("Booking not found");
    }

    return bookingStatusHistoryRepository
            .findByBookingIdOrderByChangedAtAsc(bookingId);
}

}
