package com.avis.vehicle_reservation_consumer.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;


@Entity
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(name = "booking_id")
    UUID bookingId;

    @Column(name = "timestamp")
    String timestamp;

    @Column(name = "event-type")
    String eventType;
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


    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }
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

