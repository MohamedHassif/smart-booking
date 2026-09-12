package com.smartbooking.booking.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smartbooking.booking.entity.BookingStatusHistory;

public interface BookingStatusHistoryRepository
        extends JpaRepository<BookingStatusHistory, Long> {

    List<BookingStatusHistory> findByBookingIdOrderByChangedAtAsc(Long bookingId);
}