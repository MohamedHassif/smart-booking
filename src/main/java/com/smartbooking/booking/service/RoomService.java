package com.smartbooking.booking.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smartbooking.booking.dto.RoomRequest;
import com.smartbooking.booking.dto.RoomResponse;
import com.smartbooking.booking.entity.BookingStatus;
import com.smartbooking.booking.entity.Room;
import com.smartbooking.booking.entity.RoomStatus;
import com.smartbooking.booking.exception.BookingNotFoundException;
import com.smartbooking.booking.exception.RoomNotFoundException;
import com.smartbooking.booking.exception.RoomStatusConflictException;
import com.smartbooking.booking.repository.BookingRepository;
import com.smartbooking.booking.repository.RoomRepository;

@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final BookingRepository bookingRepository;

    public RoomService(RoomRepository roomRepository, BookingRepository bookingRepository) {
        this.roomRepository = roomRepository;
        this.bookingRepository = bookingRepository;
    }

    @Transactional
    public RoomResponse createRoom(RoomRequest request) {

        if (roomRepository.existsByRoomNumber(request.getRoomNumber())) {
            throw new IllegalArgumentException(
                    "Room number already exists");
        }

        Room room = new Room();

        room.setRoomNumber(request.getRoomNumber());
        room.setRoomType(request.getRoomType());
        room.setCapacity(request.getCapacity());
        room.setPricePerNight(request.getPricePerNight());

        // Server decides the initial status
        room.setStatus(RoomStatus.AVAILABLE);

        Room savedRoom = roomRepository.save(room);

        return mapToRoomResponse(savedRoom);
    }

    @Transactional(readOnly = true)
    public List<RoomResponse> getAllRooms() {

        return roomRepository.findAll()
                .stream()
                .map(this::mapToRoomResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public RoomResponse getRoom(Long roomId) {

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() ->
                        new BookingNotFoundException("Room not found"));

        return mapToRoomResponse(room);
    }

@Transactional
public RoomResponse updateRoomStatus(Long roomId, RoomStatus status) {

    Room room = roomRepository.findById(roomId)
            .orElseThrow(() -> new RoomNotFoundException("Room not found"));

    // If trying to deactivate or put room under maintenance,
    // check whether it has active bookings
    if (status == RoomStatus.INACTIVE || status == RoomStatus.MAINTENANCE) {

        boolean hasActiveBooking = bookingRepository.existsActiveBookingForRoom(
                roomId,
                List.of(BookingStatus.PENDING, BookingStatus.CONFIRMED)
        );

        if (hasActiveBooking) {
            throw new RoomStatusConflictException(
                    "Room cannot be changed to " + status +
                    " because it has active bookings"
            );
        }
    }

    room.setStatus(status);

    Room updatedRoom = roomRepository.save(room);

    return mapToRoomResponse(updatedRoom);
}

    private RoomResponse mapToRoomResponse(Room room) {

        return new RoomResponse(
                room.getId(),
                room.getRoomNumber(),
                room.getRoomType(),
                room.getCapacity(),
                room.getPricePerNight(),
                room.getStatus()
        );
    }

    @Transactional(readOnly = true)
public List<RoomResponse> getAvailableRooms(
        LocalDate checkInDate,
        LocalDate checkOutDate,
        Integer guests) {

    if (!checkOutDate.isAfter(checkInDate)) {
        throw new IllegalArgumentException(
                "Check-out date must be after check-in date");
    }

    List<Room> rooms = roomRepository.findAvailableRooms(
            checkInDate,
            checkOutDate,
            guests,
            List.of(
                    BookingStatus.PENDING,
                    BookingStatus.CONFIRMED
            )
    );

    return rooms.stream()
            .map(this::mapToRoomResponse)
            .toList();
}

@Transactional
public RoomResponse updateRoom(
        Long roomId,
        RoomRequest request) {

    Room room = roomRepository.findById(roomId)
            .orElseThrow(() ->
                    new RoomNotFoundException("Room not found"));

    if (!room.getRoomNumber().equals(request.getRoomNumber())
            && roomRepository.existsByRoomNumber(
                    request.getRoomNumber())) {

        throw new IllegalArgumentException(
                "Room number already exists");
    }

    if (request.getCapacity() < room.getCapacity()) {

    boolean hasBookingExceedingCapacity =
            bookingRepository.existsBookingExceedingCapacity(
                    roomId,
                    request.getCapacity(),
                    List.of(
                            BookingStatus.PENDING,
                            BookingStatus.CONFIRMED
                    )
            );

    if (hasBookingExceedingCapacity) {
        throw new RoomStatusConflictException(
                "Room capacity cannot be reduced because existing bookings exceed the new capacity"
        );
    }
}

    room.setRoomNumber(request.getRoomNumber());
    room.setRoomType(request.getRoomType());
    room.setCapacity(request.getCapacity());
    room.setPricePerNight(request.getPricePerNight());

    Room updatedRoom = roomRepository.save(room);

    return mapToRoomResponse(updatedRoom);
}

}