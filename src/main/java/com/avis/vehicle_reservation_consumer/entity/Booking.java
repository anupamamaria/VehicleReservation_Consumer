package com.avis.vehicle_reservation_consumer.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

//@NamedStoredProcedureQuery(
//        name = "Booking.saveOrUpdateBooking",
//        procedureName = "save_or_update_booking",
//        parameters = {
//                @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_booking_id", type = UUID.class),
//                @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_timestamp", type = String.class),
//                @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_event_type", type = String.class),
//                @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_user_id", type = Integer.class),
//                @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_car_id", type = Integer.class),
//                @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_source_location_id", type = Integer.class),
//                @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_destination_location_id", type = Integer.class),
//                @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_start_date", type = LocalDate.class),
//                @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_end_date", type = LocalDate.class),
//                @StoredProcedureParameter(mode = ParameterMode.OUT, name = "out", type = Boolean.class)
//        }
//)
@Entity
public class Booking implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(name = "booking_id")
    UUID bookingId;

    @Column(name = "timestamp")
    String timestamp;

//    @Column(name = "event_type")
//    String eventType;
    @Column(name = "user_id")
    private int userId;
    @Column(name = "car_id")
    private int carId;
    @Column(name = "source_location_id")
    private int sourceLocationId;
    @Column(name = "destination_location_id")
    private int destinationLocationId;
    @Column(name = "start_date")
    private LocalDate startDate;
    @Column(name = "end_date")
    private LocalDate endDate;


//    public String getEventType() {
//        return eventType;
//    }
//
//    public void setEventType(String eventType) {
//        this.eventType = eventType;
//    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UUID getBookingId() {
        return bookingId;
    }

    public void setBookingId(UUID bookingId) {
        this.bookingId = bookingId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public int getSourceLocationId() {
        return sourceLocationId;
    }

    public void setSourceLocationId(int sourceLocationId) {
        this.sourceLocationId = sourceLocationId;
    }

    public int getDestinationLocationId() {
        return destinationLocationId;
    }

    public void setDestinationLocationId(int destinationLocationId) {
        this.destinationLocationId = destinationLocationId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}

