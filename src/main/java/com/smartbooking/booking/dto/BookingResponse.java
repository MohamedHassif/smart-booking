package com.smartbooking.booking.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.smartbooking.booking.entity.BookingStatus;

public class BookingResponse {

    private Long id;
    private Long roomId;
    private String roomNumber;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private BookingStatus status;
    private Integer numberOfGuests;
    private LocalDateTime createdAt;
    private Long userId;

    public BookingResponse(
            Long id,
            Long roomId,
            String roomNumber,
            LocalDate checkInDate,
            LocalDate checkOutDate,
            BookingStatus status,
            Integer numberOfGuests,
            LocalDateTime createdAt,
            Long userId) {

        this.id = id;
        this.roomId = roomId;
        this.roomNumber = roomNumber;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.status = status;
        this.numberOfGuests = numberOfGuests;
        this.createdAt = createdAt;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public Long getRoomId() {
        return roomId;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
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