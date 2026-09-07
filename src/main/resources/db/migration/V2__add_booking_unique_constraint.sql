ALTER TABLE bookings
    ADD CONSTRAINT uk_booking_user_date
        UNIQUE (user_id, booking_date);