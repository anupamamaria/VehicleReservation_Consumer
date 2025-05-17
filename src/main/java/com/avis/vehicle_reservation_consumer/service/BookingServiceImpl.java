package com.avis.vehicle_reservation_consumer.service;

import com.avis.vehicle_reservation_consumer.dto.BookingDTO;
import com.avis.vehicle_reservation_consumer.entity.Booking;
import com.avis.vehicle_reservation_consumer.repository.BookingRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BookingServiceImpl implements BookingService{

    private final BookingRepository bookingRepository;

    public BookingServiceImpl(BookingRepository bookingRepository){
        this.bookingRepository = bookingRepository;
    }
    @Override
    public void saveBooking(UUID bookingId, String timestamp, BookingDTO bookingDTO, String eventType){
        Booking booking = new Booking();
        booking.setBookingId(bookingId);
        booking.setTimestamp(timestamp);
        booking.setUserId(bookingDTO.getUserId());
        booking.setCarId(bookingDTO.getCarId());
        booking.setStartDate(bookingDTO.getStartDate());
        booking.setEndDate(bookingDTO.getEndDate());
        booking.setSourceLocationId(bookingDTO.getSourceLocationId());
        booking.setDestinationLocationId(bookingDTO.getDestinationLocationId());
        booking.setEventType(eventType);
        bookingRepository.save(booking);
    }

    @Override
    public void updateBooking(UUID bookingId, String timestamp, BookingDTO bookingDTO, String eventType){
        Optional<Booking> optionalBooking = bookingRepository.findByBookingId(bookingId);
        if(optionalBooking.isPresent()){
            Booking booking = optionalBooking.get();
            booking.setTimestamp(timestamp);
            booking.setCarId(bookingDTO.getCarId());
            booking.setStartDate(bookingDTO.getStartDate());
            booking.setEndDate(bookingDTO.getEndDate());
            booking.setSourceLocationId(bookingDTO.getSourceLocationId());
            booking.setDestinationLocationId(bookingDTO.getDestinationLocationId());
            booking.setEventType(eventType);
            bookingRepository.save(booking);
        }
    }

    @Override
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }
}
