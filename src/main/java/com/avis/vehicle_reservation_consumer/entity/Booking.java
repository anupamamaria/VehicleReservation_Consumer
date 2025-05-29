package com.avis.vehicle_reservation_consumer.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

//    @Column(name = "event-type")
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
}

