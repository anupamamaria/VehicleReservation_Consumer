package com.avis.vehicle_reservation_consumer.repository;

import com.avis.vehicle_reservation_consumer.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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
List<Booking> findBySourceLocationIdAndUserId(int sourceLocationId, int userId);
List<Booking> findByDestinationLocationIdAndUserId(int destinationLocationId, int userId);
    @Query(value = "SELECT save_or_update_booking(:bookingId, :timestamp, :userId, :carId," +
            " :sourceLocationId, :destinationLocationId, :startDate, :endDate)", nativeQuery = true)
    Boolean callSaveOrUpdateBooking(@Param("bookingId") UUID bookingId,
                                    @Param("timestamp") String timestamp,
                                    @Param("userId") int userId,
                                    @Param("carId") int carId,
                                    @Param("sourceLocationId") int sourceLocationId,
                                    @Param("destinationLocationId") int destinationLocationId,
                                    @Param("startDate") LocalDate startDate,
                                    @Param("endDate") LocalDate endDate);
}
