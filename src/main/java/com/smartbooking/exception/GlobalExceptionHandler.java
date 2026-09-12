package com.smartbooking.exception;

import java.util.HashMap;
import java.util.Map;

import org.postgresql.util.PSQLException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.smartbooking.booking.exception.BookingConflictException;
import com.smartbooking.booking.exception.BookingNotFoundException;
import com.smartbooking.booking.exception.BookingOperationException;
import com.smartbooking.booking.exception.RoomNotFoundException;
import com.smartbooking.booking.exception.RoomStatusConflictException;
import com.smartbooking.dto.ErrorResponse;

import jakarta.persistence.OptimisticLockException;
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(
            MethodArgumentNotValidException exception) {

        Map<String, String> errors = new HashMap<>();

        exception.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        errors.put(
                                error.getField(),
                                error.getDefaultMessage()
                        )
                );

        return new ErrorResponse(
                400,
                "Validation failed",
                errors
        );
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserNotFound(
            UserNotFoundException exception) {

        return new ErrorResponse(
                404,
                exception.getMessage(),
                null
        );
    }


    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleAuthenticationException(AuthenticationException authenticationException){
        return new ErrorResponse(
                401,
                "Invalid email or password",
                null
        );
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(Exception exception) {

        exception.printStackTrace();

        return new ErrorResponse(
                500,
                "An Error occured at server end",
                null
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleAccessDeniedException(
            AccessDeniedException exception) {

        return new ErrorResponse(
                403,
                "Access denied",
                null
        );
    }

    @ExceptionHandler(BookingNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleBookingNotFound(BookingNotFoundException exception){

        return new ErrorResponse(
                404,
                exception.getMessage(),
                null
        );
    }

    @ExceptionHandler(BookingOperationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBookingOperation(
            BookingOperationException exception) {

        return new ErrorResponse(
                400,
                exception.getMessage(),
                null
        );
    }

    @ExceptionHandler(OptimisticLockException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleOptimisticLockException(
            OptimisticLockException exception) {

        return new ErrorResponse(
                409,
                "Booking was modified by another user. Please refresh and try again",
                null
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIllegalArgumentException(IllegalArgumentException exception){

        return new ErrorResponse(
                400,
                exception.getMessage(),null
        );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleTypeMismatch(
            MethodArgumentTypeMismatchException ex) {

        String parameter = ex.getName();


        return new ErrorResponse(
                400,
                "Invalid value for parameter: ",null
        );
    }


    @ExceptionHandler(BookingConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleBookingConflict(BookingConflictException exception){
        return new ErrorResponse(
                409,
                exception.getMessage(),null
        );
    }

        @ExceptionHandler(RoomNotFoundException.class)
        @ResponseStatus(HttpStatus.NOT_FOUND)
        public ErrorResponse handleRoomNotFoundException(RoomNotFoundException exception){
        return new ErrorResponse(
                404,
                exception.getMessage(),null
        );
    }

        @ExceptionHandler(RoomStatusConflictException.class)
        @ResponseStatus(HttpStatus.CONFLICT)
        public ErrorResponse handleRoomStatusConflict(RoomStatusConflictException exception){
        return new ErrorResponse(
                409,
                exception.getMessage(),null
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
@ResponseStatus(HttpStatus.CONFLICT)
public ErrorResponse handleDataIntegrityViolation(
        DataIntegrityViolationException exception) {

    Throwable cause = exception;

    while (cause != null) {

        if (cause instanceof PSQLException psqlException) {

            String constraintName =
                    psqlException.getServerErrorMessage().getConstraint();

            if ("ex_booking_room_date_overlap".equals(constraintName)) {
                return new ErrorResponse(
                        409,
                        "Room is already booked for the selected dates",
                        null
                );
            }
        }

        cause = cause.getCause();
    }

    return new ErrorResponse(
            409,
            "Database constraint violation",
            null
    );
}

}