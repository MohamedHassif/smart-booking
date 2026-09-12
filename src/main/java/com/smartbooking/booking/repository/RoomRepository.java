package com.smartbooking.booking.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.smartbooking.booking.entity.BookingStatus;
import com.smartbooking.booking.entity.Room;

public interface RoomRepository extends JpaRepository<Room, Long> {

    Optional<Room> findByRoomNumber(String roomNumber);

    boolean existsByRoomNumber(String roomNumber);

    @Query("""
    SELECT r
    FROM Room r
    WHERE r.status = com.smartbooking.booking.entity.RoomStatus.AVAILABLE
      AND r.capacity >= :guests
      AND NOT EXISTS (
          SELECT b.id
          FROM Booking b
          WHERE b.room.id = r.id
            AND b.status IN :statuses
            AND b.checkInDate < :checkOutDate
            AND b.checkOutDate > :checkInDate
      )
""")
List<Room> findAvailableRooms(
        @Param("checkInDate") LocalDate checkInDate,
        @Param("checkOutDate") LocalDate checkOutDate,
        @Param("guests") Integer guests,
        @Param("statuses") List<BookingStatus> statuses
);


}