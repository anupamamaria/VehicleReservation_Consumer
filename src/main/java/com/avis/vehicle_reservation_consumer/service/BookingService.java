package com.avis.vehicle_reservation_consumer.service;

import com.avis.vehicle_reservation_consumer.dto.BookingDTO;
import com.avis.vehicle_reservation_consumer.entity.Booking;

import java.util.List;
import java.util.UUID;

public interface BookingService {
     List<Booking> findBookingsBySourceLocationAndUserId(int sourceLocationId, int userId);
     List<Booking> findBookingsByDestinationLocationAndUserId(int destinationLocationId, int userId);
     void deleteBookingById(UUID bookingId);
     List<Booking> getRecentBookings(int userId);
     void processBookingEvent(UUID bookingId, String timestamp, BookingDTO bookingDTO);
}