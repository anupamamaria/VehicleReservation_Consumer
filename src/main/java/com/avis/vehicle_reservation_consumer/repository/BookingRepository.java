package com.avis.vehicle_reservation_consumer.repository;

import com.avis.vehicle_reservation_consumer.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BookingRepository extends JpaRepository<Booking,Integer> {
Optional<Booking> findByBookingId(UUID bookingId);
}
