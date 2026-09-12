package com.smartbooking.booking.entity;

public enum BookingStatus {

    PENDING,
    CONFIRMED,
    CANCELLED,
    COMPLETED;

    public boolean canBeUpdated() {
        return this == PENDING;
    }

    public boolean canBeConfirmed() {
        return this == PENDING;
    }

    public boolean canBeCancelled() {
        return this == PENDING || this == CONFIRMED;
    }

    public boolean canBeCompleted() {
        return this == CONFIRMED;
    }
}