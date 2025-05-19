package com.avis.vehicle_reservation_consumer.service;
import com.avis.vehicle_reservation_consumer.dto.BookingDTO;
import com.avis.vehicle_reservation_consumer.entity.Booking;
import com.avis.vehicle_reservation_consumer.repository.BookingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BookingServiceImpl implements BookingService{

    private final BookingRepository bookingRepository;
    private static final Logger logger = LoggerFactory.getLogger(BookingServiceImpl.class);

    public BookingServiceImpl(BookingRepository bookingRepository){
        this.bookingRepository = bookingRepository;
    }

    @Override
    public void saveBooking(UUID bookingId, String timestamp, BookingDTO bookingDTO, String eventType){
        validateBooking(bookingId, bookingDTO, eventType);
        Booking booking = new Booking();
        booking.setBookingId(bookingId);
        assignBookingFields(booking, timestamp, bookingDTO, eventType);
        logger.info("Saving the booking in database");
        bookingRepository.save(booking);
    }

    @Override
    public void updateBooking(UUID bookingId, String timestamp, BookingDTO bookingDTO, String eventType){
        validateBooking(bookingId, bookingDTO, eventType);
        Optional<Booking> optionalBooking = bookingRepository.findByBookingId(bookingId);
        if(optionalBooking.isPresent()){
            Booking booking = optionalBooking.get();
            assignBookingFields(booking, timestamp, bookingDTO, eventType);
            logger.info("Updating the booking in database");
            bookingRepository.save(booking);
        }
        else{
            logger.error("Booking with Id: "+bookingId+" is not found for updating");
        }
    }

    public void assignBookingFields(Booking booking, String timestamp, BookingDTO bookingDTO, String eventType){
        booking.setTimestamp(timestamp);
        booking.setUserId(bookingDTO.getUserId());
        booking.setCarId(bookingDTO.getCarId());
        booking.setStartDate(bookingDTO.getStartDate());
        booking.setEndDate(bookingDTO.getEndDate());
        booking.setSourceLocationId(bookingDTO.getSourceLocationId());
        booking.setDestinationLocationId(bookingDTO.getDestinationLocationId());
        booking.setEventType(eventType);
    }

    public void validateBooking(UUID bookingId, BookingDTO bookingDTO, String eventType) {
        if (bookingId == null) {
            throw new IllegalArgumentException("Booking Id must not be null");
        }
        if (eventType == null || eventType.trim().isEmpty()) {
            throw new IllegalArgumentException("Event Type must not be null or empty");
        }
        if (bookingDTO == null) {
            throw new IllegalArgumentException("Booking data must not be null");
        }
        if(bookingDTO.getUserId() == null ||
                bookingDTO.getCarId() == null ||
                bookingDTO.getSourceLocationId() == null ||
                bookingDTO.getDestinationLocationId() == null ||
                bookingDTO.getStartDate() == null ||
                bookingDTO.getEndDate() == null){
            throw new IllegalArgumentException("One or more required booking fields are null");
        }
    }

    @Override
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    @Override
    public List<Booking> findBookingsBySourceLocation(int sourceLocationId){
        return bookingRepository.findBySourceLocationId(sourceLocationId);
    }

    @Override
    public List<Booking> findBookingsByDestinationLocation(int destinationLocationId){
        return bookingRepository.findByDestinationLocationId(destinationLocationId);
    }
}
