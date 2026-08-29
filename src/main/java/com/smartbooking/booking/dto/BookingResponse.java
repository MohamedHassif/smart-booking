package com.smartbooking.booking.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.smartbooking.booking.entity.BookingStatus;

public class BookingResponse {

    private Long id;
    private LocalDate bookingDate;
    private BookingStatus status;
    private Integer numberOfGuests;
    private LocalDateTime createdAt;
    private Long userId;

    public BookingResponse(
            Long id,
            LocalDate bookingDate,
            BookingStatus status,
            Integer numberOfGuests,
            LocalDateTime createdAt,
            Long userId) {

        this.id = id;
        this.bookingDate = bookingDate;
        this.status = status;
        this.numberOfGuests = numberOfGuests;
        this.createdAt = createdAt;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Long getUserId() {
        return userId;
    }
}