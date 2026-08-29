package com.smartbooking.booking.repository;

import com.smartbooking.booking.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking,Long> {
}
