CREATE OR REPLACE FUNCTION save_or_update_booking(
    p_booking_id UUID,
    p_timestamp TEXT,
    p_event_type TEXT,
    p_user_id INTEGER,
    p_car_id INTEGER,
    p_source_location_id INTEGER,
    p_destination_location_id INTEGER,
    p_start_date DATE,
    p_end_date DATE
--    OUT location_changed BOOLEAN
)
RETURNS BOOLEAN AS $$
DECLARE
    existing_source INTEGER;
    existing_destination INTEGER;
    location_changed BOOLEAN;
BEGIN
    -- Try to find existing booking
    SELECT source_location_id, destination_location_id
    INTO existing_source, existing_destination
    FROM bookings
    WHERE booking_id = p_booking_id;

    IF FOUND THEN
        -- Update existing booking
        UPDATE bookings SET
            timestamp = p_timestamp,
            event_type = p_event_type,
            user_id = p_user_id,
            car_id = p_car_id,
            source_location_id = p_source_location_id,
            destination_location_id = p_destination_location_id,
            start_date = p_start_date,
            end_date = p_end_date
        WHERE booking_id = p_booking_id;

        -- Determine if locations have changed
        location_changed :=
            (existing_source IS DISTINCT FROM p_source_location_id OR
             existing_destination IS DISTINCT FROM p_destination_location_id);
    ELSE
        -- Insert new booking
        INSERT INTO bookings (
            booking_id, timestamp, event_type, user_id, car_id,
            source_location_id, destination_location_id, start_date, end_date
        )
        VALUES (
            p_booking_id, p_timestamp, p_event_type, p_user_id, p_car_id,
            p_source_location_id, p_destination_location_id, p_start_date, p_end_date
        );

        location_changed := FALSE;
    END IF;

    RETURN location_changed;
END;
$$ LANGUAGE plpgsql;