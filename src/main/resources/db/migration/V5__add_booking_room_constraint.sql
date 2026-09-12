ALTER TABLE bookings
ALTER COLUMN room_id SET NOT NULL;

ALTER TABLE bookings
ADD CONSTRAINT fk_booking_room
FOREIGN KEY (room_id)
REFERENCES rooms(id);