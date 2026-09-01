package com.smartbooking.booking.specification;

import java.time.LocalDate;

import org.springframework.data.jpa.domain.Specification;

import com.smartbooking.booking.entity.Booking;
import com.smartbooking.booking.entity.BookingStatus;

public class BookingSpecification {

    public static Specification<Booking> hasStatus(BookingStatus status) {
        return (root, query, criteriaBuilder) -> {

            if (status == null) {
                return null;
            }

            return criteriaBuilder.equal(
                    root.get("status"),
                    status
            );
        };
    }

    public static Specification<Booking> fromDate(LocalDate fromDate) {
        return (root, query, criteriaBuilder) -> {

            if (fromDate == null) {
                return null;
            }

            return criteriaBuilder.greaterThanOrEqualTo(
                    root.get("bookingDate"),
                    fromDate
            );
        };
    }

    public static Specification<Booking> toDate(LocalDate toDate) {
        return (root, query, criteriaBuilder) -> {

            if (toDate == null) {
                return null;
            }

            return criteriaBuilder.lessThanOrEqualTo(
                    root.get("bookingDate"),
                    toDate
            );
        };
    }
}