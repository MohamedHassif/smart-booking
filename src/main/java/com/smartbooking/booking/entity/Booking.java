package com.smartbooking.booking.entity;


import java.time.LocalDate;
import java.time.LocalDateTime;

import com.smartbooking.entity.User;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

private LocalDate checkInDate;

private LocalDate checkOutDate;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    private Integer numberOfGuests;

    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)//Load the associated User when it is actually needed.
    @JoinColumn(name = "user_id",nullable = false) //The bookings.user_id column is the foreign key that connects this booking to the User.
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @Version
    private Long version;

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setNumberOfGuests(Integer numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public LocalDate getCheckInDate() {
    return checkInDate;
}

public void setCheckInDate(LocalDate checkInDate) {
    this.checkInDate = checkInDate;
}

public LocalDate getCheckOutDate() {
    return checkOutDate;
}

public void setCheckOutDate(LocalDate checkOutDate) {
    this.checkOutDate = checkOutDate;
}

public Room getRoom() {
    return room;
}

public void setRoom(Room room) {
    this.room = room;
}
}