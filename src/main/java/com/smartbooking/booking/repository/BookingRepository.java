package com.smartbooking.booking.repository;

import com.smartbooking.booking.entity.Booking;
import com.smartbooking.booking.entity.BookingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking,Long>,
        JpaSpecificationExecutor<Booking> {

    List<Booking> findByUserId(Long userId);

    Optional<Booking> findByIdAndUserId(Long id, Long userId);

    @Query("""
        SELECT b
        FROM Booking b
        WHERE (:status IS NULL OR b.status = :status)
        AND (:fromDate IS NULL OR b.bookingDate >= :fromDate)
        AND (:toDate IS NULL OR b.bookingDate <= :toDate)
        """)
    Page<Booking> searchBookings(
            @Param("status") BookingStatus status,
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate,
            Pageable pageable);
}


