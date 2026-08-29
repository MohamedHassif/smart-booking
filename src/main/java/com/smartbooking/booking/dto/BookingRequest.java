package com.smartbooking.booking.dto;


import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class BookingRequest {



    @NotNull(message = "Booking date is required")
    @FutureOrPresent(message = "Booking date must be today or in the future")
    private LocalDate bookingDate;

    @NotNull(message = "Number of Guest is required")
    @Min(value=1,message="Number of guests must be at least 1")
    private Integer numberOfGuests;

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }

    public Integer getNumberOfGuest() {
        return numberOfGuests;
    }

    public void setNumberOfGuest(Integer numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }
}
