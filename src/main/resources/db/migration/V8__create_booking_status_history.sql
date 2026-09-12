CREATE TABLE booking_status_history (
    id BIGSERIAL PRIMARY KEY,

    booking_id BIGINT NOT NULL,

    old_status VARCHAR(30),

    new_status VARCHAR(30) NOT NULL,

    changed_by BIGINT,

    changed_at TIMESTAMP NOT NULL,

    CONSTRAINT fk_history_booking
        FOREIGN KEY (booking_id)
        REFERENCES bookings(id),

    CONSTRAINT fk_history_user
        FOREIGN KEY (changed_by)
        REFERENCES users(id)
);