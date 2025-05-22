package com.avis.vehicle_reservation_consumer.service;
import com.avis.vehicle_reservation_consumer.dto.BookingDTO;
import com.avis.vehicle_reservation_consumer.entity.Booking;
import com.avis.vehicle_reservation_consumer.repository.BookingRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Date;
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

//    @Override
//    public void saveBooking(UUID bookingId, String timestamp, BookingDTO bookingDTO){
//        validateBooking(bookingId, bookingDTO);
//        Booking booking = new Booking();
//        booking.setBookingId(bookingId);
//        assignBookingFields(booking, timestamp, bookingDTO);
//        logger.info("Saving the booking in database");
//        bookingRepository.save(booking);
//    }
//
//    @Override
//    public void updateBooking(UUID bookingId, String timestamp, BookingDTO bookingDTO, String eventType){
//        validateBooking(bookingId, bookingDTO);
//        Optional<Booking> optionalBooking = bookingRepository.findByBookingId(bookingId);
//        if(optionalBooking.isPresent()){
//            Booking booking = optionalBooking.get();
//            assignBookingFields(booking, timestamp, bookingDTO);
//            logger.info("Updating the booking in database");
//            bookingRepository.save(booking);
//        }
//        else{
//            logger.error("Booking with Id: "+bookingId+" is not found for updating");
//        }
//    }
//
//    public void assignBookingFields(Booking booking, String timestamp, BookingDTO bookingDTO){
//        booking.setTimestamp(timestamp);
//        booking.setUserId(bookingDTO.getUserId());
//        booking.setCarId(bookingDTO.getCarId());
//        booking.setStartDate(bookingDTO.getStartDate());
//        booking.setEndDate(bookingDTO.getEndDate());
//        booking.setSourceLocationId(bookingDTO.getSourceLocationId());
//        booking.setDestinationLocationId(bookingDTO.getDestinationLocationId());
//    }

    public void validateBooking(UUID bookingId, BookingDTO bookingDTO) {
        if (bookingId == null) {
            throw new IllegalArgumentException("Booking Id must not be null");
        }
//        if (eventType == null || eventType.trim().isEmpty()) {
//            throw new IllegalArgumentException("Event Type must not be null or empty");
//        }
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

    @PersistenceContext
    private EntityManager entityManager;
    @Override
    public void processBookingEvent(UUID bookingId, String timestamp, BookingDTO bookingDTO) {
        validateBooking(bookingId,bookingDTO);
    try {
        // Use named parameters for better readability and safety
        Query query = entityManager.createNativeQuery(
                "SELECT save_or_update_booking(:bookingId, :timestamp, :userId, :carId, " +
                        ":sourceLocationId, :destinationLocationId, :startDate, :endDate)"
        );

        query.setParameter("bookingId", bookingId);
        query.setParameter("timestamp", timestamp);
//        query.setParameter("eventType", eventType);
        query.setParameter("userId", bookingDTO.getUserId());
        query.setParameter("carId", bookingDTO.getCarId());
        query.setParameter("sourceLocationId", bookingDTO.getSourceLocationId());
        query.setParameter("destinationLocationId", bookingDTO.getDestinationLocationId());
        query.setParameter("startDate", Date.valueOf(bookingDTO.getStartDate()));
        query.setParameter("endDate", Date.valueOf(bookingDTO.getEndDate()));

        Boolean criticalFieldChanged = (Boolean) query.getSingleResult();
        System.out.println("Function returned: " + criticalFieldChanged);

        // If locations changed, you might want to handle that here
        if (criticalFieldChanged) {
            System.out.println("Critical Fields changed: " + bookingId);
            // Add any special handling for location changes
        }
    } catch (Exception e) {
        System.err.println("Error calling save_or_update_booking function: " + e.getMessage());
        e.printStackTrace();
        throw new RuntimeException("Failed to process booking event: " + e.getMessage(), e);
    }
}

}
