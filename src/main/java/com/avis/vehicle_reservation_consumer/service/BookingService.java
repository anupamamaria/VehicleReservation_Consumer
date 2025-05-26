package com.avis.vehicle_reservation_consumer.service;

import com.avis.vehicle_reservation_consumer.dto.BookingDTO;
import com.avis.vehicle_reservation_consumer.entity.Booking;

import java.util.List;
import java.util.UUID;

public interface BookingService {
//     void saveBooking(UUID bookingId, String timestamp, BookingDTO bookingDTO);
//     void updateBooking(UUID bookingId, String timestamp, BookingDTO bookingDTO, String eventType);
     List<Booking> getAllBookings();
     List<Booking> findBookingsBySourceLocation(int sourceLocationId);
     List<Booking> findBookingsByDestinationLocation(int destinationLocationId);
     void deleteBookingById(UUID bookingId);
     List<Booking> getRecentBookings();
     void processBookingEvent(UUID bookingId, String timestamp, BookingDTO bookingDTO);
}
