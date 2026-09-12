CREATE INDEX idx_bookings_room_status
ON bookings (room_id, status);

CREATE INDEX idx_bookings_check_in_date
ON bookings (check_in_date);

CREATE INDEX idx_bookings_check_out_date
ON bookings (check_out_date);