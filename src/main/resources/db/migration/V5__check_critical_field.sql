CREATE OR REPLACE FUNCTION save_or_update_booking(
    p_booking_id UUID,
    p_timestamp TEXT,
    p_user_id INTEGER,
    p_car_id INTEGER,
    p_source_location_id INTEGER,
    p_destination_location_id INTEGER,
    p_start_date DATE,
    p_end_date DATE
)
RETURNS BOOLEAN AS $$
DECLARE
    existing_source INTEGER;
    existing_destination INTEGER;
    existing_car INTEGER;
    critical_field_changed BOOLEAN := FALSE;
BEGIN

    SELECT source_location_id, destination_location_id, car_id
    INTO existing_source, existing_destination, existing_car
    FROM booking
    WHERE booking_id = p_booking_id;

    IF FOUND THEN

        UPDATE booking SET
            timestamp = p_timestamp,
            user_id = p_user_id,
            car_id = p_car_id,
            source_location_id = p_source_location_id,
            destination_location_id = p_destination_location_id,
            start_date = p_start_date,
            end_date = p_end_date
        WHERE booking_id = p_booking_id;

        -- Check if critical fields have changed
                critical_field_changed :=
                    (existing_source IS DISTINCT FROM p_source_location_id OR
                     existing_destination IS DISTINCT FROM p_destination_location_id OR
                     existing_car IS DISTINCT FROM p_car_id);
    ELSE
        -- Insert new booking
        INSERT INTO booking (
            booking_id, timestamp, user_id, car_id,
            source_location_id, destination_location_id, start_date, end_date
        )
        VALUES (
            p_booking_id, p_timestamp, p_user_id, p_car_id,
            p_source_location_id, p_destination_location_id, p_start_date, p_end_date
        );

        critical_field_changed := FALSE;
    END IF;

    RETURN critical_field_changed;
END;
$$ LANGUAGE plpgsql;