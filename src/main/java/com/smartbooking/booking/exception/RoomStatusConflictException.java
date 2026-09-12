package com.smartbooking.booking.exception;

public class RoomStatusConflictException extends RuntimeException {

    public RoomStatusConflictException(String message) {
        super(message);
    }
}