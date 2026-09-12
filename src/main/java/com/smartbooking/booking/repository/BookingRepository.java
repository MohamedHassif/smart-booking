package com.smartbooking.booking.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.smartbooking.booking.entity.Booking;
import com.smartbooking.booking.entity.BookingStatus;

public interface BookingRepository extends JpaRepository<Booking,Long>,
        JpaSpecificationExecutor<Booking> {

    List<Booking> findByUserId(Long userId);

    Page<Booking> findByUserId(Long userId,Pageable pageable);

    Optional<Booking> findByIdAndUserId(Long id, Long userId);

//     boolean existsByUserIdAndBookingDate(
//             Long userId,
//             LocalDate bookingDate
//     );

//     boolean existsByUserIdAndBookingDateAndIdNot(
//             Long userId,
//             LocalDate bookingDate,
//             Long id
//     );

// @Query("""
//     SELECT COUNT(b) > 0
//     FROM Booking b
//     WHERE b.id <> :bookingId
//       AND b.room.id = :roomId
//       AND b.status IN :statuses
//       AND b.checkInDate < :checkOutDate
//       AND b.checkOutDate > :checkInDate
// """)
// boolean existsOverlappingBooking(
//         @Param("bookingId") Long bookingId,
//         @Param("roomId") Long roomId,
//         @Param("checkInDate") LocalDate checkInDate,
//         @Param("checkOutDate") LocalDate checkOutDate,
//         @Param("statuses") List<BookingStatus> statuses
// );



//    @Query("""
//        SELECT b
//        FROM Booking b
//        WHERE (:status IS NULL OR b.status = :status)
//        AND (:fromDate IS NULL OR b.bookingDate >= :fromDate)
//        AND (:toDate IS NULL OR b.bookingDate <= :toDate)
//        """)
//    Page<Booking> searchBookings(
//            @Param("status") BookingStatus status,
//            @Param("fromDate") LocalDate fromDate,
//            @Param("toDate") LocalDate toDate,
//            Pageable pageable);

@Query("""
    SELECT COUNT(b) > 0
    FROM Booking b
    WHERE b.room.id = :roomId
      AND b.status IN :statuses
      AND b.checkInDate < :checkOutDate
      AND b.checkOutDate > :checkInDate
""")
boolean existsOverlappingBooking(
        @Param("roomId") Long roomId,
        @Param("checkInDate") LocalDate checkInDate,
        @Param("checkOutDate") LocalDate checkOutDate,
        @Param("statuses") List<BookingStatus> statuses
);

@Query("""
    SELECT COUNT(b) > 0
    FROM Booking b
    WHERE b.id <> :bookingId
      AND b.room.id = :roomId
      AND b.status IN :statuses
      AND b.checkInDate < :checkOutDate
      AND b.checkOutDate > :checkInDate
""")
boolean existsOverlappingBookingForUpdate(
        @Param("bookingId") Long bookingId,
        @Param("roomId") Long roomId,
        @Param("checkInDate") LocalDate checkInDate,
        @Param("checkOutDate") LocalDate checkOutDate,
        @Param("statuses") List<BookingStatus> statuses
);

@Query("""
    SELECT COUNT(b) > 0
    FROM Booking b
    WHERE b.room.id = :roomId
      AND b.status IN :statuses
""")
boolean existsActiveBookingForRoom(
        @Param("roomId") Long roomId,
        @Param("statuses") List<BookingStatus> statuses
);

@Query("""
    SELECT COUNT(b) > 0
    FROM Booking b
    WHERE b.room.id = :roomId
      AND b.status IN :statuses
      AND b.numberOfGuests > :capacity
""")
boolean existsBookingExceedingCapacity(
        @Param("roomId") Long roomId,
        @Param("capacity") Integer capacity,
        @Param("statuses") List<BookingStatus> statuses
);


@Query("""
    SELECT b
    FROM Booking b
    WHERE b.status = :status
      AND b.checkOutDate <= :date
""")
List<Booking> findBookingsForCompletion(
        @Param("status") BookingStatus status,
        @Param("date") LocalDate date
);

}


