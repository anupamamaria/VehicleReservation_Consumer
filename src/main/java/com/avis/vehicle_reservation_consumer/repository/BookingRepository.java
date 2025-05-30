package com.avis.vehicle_reservation_consumer.repository;

import com.avis.vehicle_reservation_consumer.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BookingRepository extends JpaRepository<Booking,Integer> {
Optional<Booking> findByBookingId(UUID bookingId);
List<Booking> findByUserId(int userId);
List<Booking> findBySourceLocationId(int sourceLocationId);
List<Booking> findByDestinationLocationId(int destinationLocationId);
}
