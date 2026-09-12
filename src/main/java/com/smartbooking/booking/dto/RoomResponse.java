package com.smartbooking.booking.dto;

import java.math.BigDecimal;

import com.smartbooking.booking.entity.RoomStatus;
import com.smartbooking.booking.entity.RoomType;

public class RoomResponse {

    private Long id;
    private String roomNumber;
    private RoomType roomType;
    private Integer capacity;
    private BigDecimal pricePerNight;
    private RoomStatus status;

    public RoomResponse(
            Long id,
            String roomNumber,
            RoomType roomType,
            Integer capacity,
            BigDecimal pricePerNight,
            RoomStatus status) {

        this.id = id;
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.capacity = capacity;
        this.pricePerNight = pricePerNight;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public BigDecimal getPricePerNight() {
        return pricePerNight;
    }

    public RoomStatus getStatus() {
        return status;
    }
}