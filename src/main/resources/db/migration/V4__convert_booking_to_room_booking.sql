-- Add new columns
ALTER TABLE bookings
ADD COLUMN check_in_date DATE;

ALTER TABLE bookings
ADD COLUMN check_out_date DATE;

ALTER TABLE bookings
ADD COLUMN room_id BIGINT;

-- Copy the existing booking date into both dates
UPDATE bookings
SET check_in_date = booking_date,
    check_out_date = booking_date;

-- Remove the old column
ALTER TABLE bookings
DROP COLUMN booking_date;