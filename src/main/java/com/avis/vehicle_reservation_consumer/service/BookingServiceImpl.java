package com.avis.vehicle_reservation_consumer.service;
import com.avis.vehicle_reservation_consumer.dto.BookingDTO;
import com.avis.vehicle_reservation_consumer.entity.Booking;
import com.avis.vehicle_reservation_consumer.model.User;
import com.avis.vehicle_reservation_consumer.repository.BookingRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.sql.Date;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService{

    private final BookingRepository bookingRepository;
    private final MailService mailService;
    private final RestTemplate restTemplate;
    private static final Logger logger = LoggerFactory.getLogger(BookingServiceImpl.class);
    private final BookingService bookingService;

    public BookingServiceImpl(BookingRepository bookingRepository,MailService mailService,
                              RestTemplate restTemplate, @Lazy BookingService bookingService){
        this.bookingRepository = bookingRepository;
        this.mailService = mailService;
        this.restTemplate = restTemplate;
        this.bookingService = bookingService;
    }

    public void validateBooking(UUID bookingId, BookingDTO bookingDTO) {
        if (bookingId == null) {
            throw new IllegalArgumentException("Booking Id must not be null");
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
//    @Cacheable("allBookings")
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();

    }

    @Override
    @Cacheable("recentBookings")
    public List<Booking> getRecentBookings() {
        List<Booking> allBookings = bookingRepository.findAll();
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);
        return allBookings.stream()
                .filter(booking -> {
                    try {
                        Instant instant = Instant.parse(booking.getTimestamp());
                        LocalDateTime bookingTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
                        return bookingTime.isAfter(oneWeekAgo);
                    } catch (Exception e) {
                        // log error if needed
                        return false;
                    }
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<Booking> findBookingsBySourceLocation(int sourceLocationId){
        return bookingRepository.findBySourceLocationId(sourceLocationId);
    }

    @Override
    public List<Booking> findBookingsByDestinationLocation(int destinationLocationId){
        return bookingRepository.findByDestinationLocationId(destinationLocationId);
    }

    @Override
//    @CacheEvict(value = "allBookings", allEntries = true)
    public void deleteBookingById(UUID bookingId){
//        logger.info("Evicting cache: allBookings");
        Optional<Booking> optionalBooking = bookingRepository.findByBookingId(bookingId);
        if(optionalBooking.isPresent()){
            Booking booking = optionalBooking.get();
            bookingRepository.delete(booking);
            logger.info("Deleted booking with booking id: {}",bookingId);
            bookingService.getAllBookings();
        }
       else{
           logger.error("Booking with booking id: {} is not found",bookingId);
        }
    }

    @PersistenceContext
    private EntityManager entityManager;
    @Override
//    @CacheEvict(value = "allBookings", allEntries = true)
    public void processBookingEvent(UUID bookingId, String timestamp, BookingDTO bookingDTO) {
//        logger.info("Evicting cache: allBookings");
        validateBooking(bookingId,bookingDTO);
        String status = "";
        if(bookingRepository.findByBookingId(bookingId).isPresent()){
            status = "updated";
        }
        else{
            status = "created";
        }

    try {
        // Use named parameters for better readability and safety
        Query query = entityManager.createNativeQuery(
                "SELECT save_or_update_booking(:bookingId, :timestamp, :userId, :carId, " +
                        ":sourceLocationId, :destinationLocationId, :startDate, :endDate)"
        );

        query.setParameter("bookingId", bookingId);
        query.setParameter("timestamp", timestamp);
        query.setParameter("userId", bookingDTO.getUserId());
        query.setParameter("carId", bookingDTO.getCarId());
        query.setParameter("sourceLocationId", bookingDTO.getSourceLocationId());
        query.setParameter("destinationLocationId", bookingDTO.getDestinationLocationId());
        query.setParameter("startDate", Date.valueOf(bookingDTO.getStartDate()));
        query.setParameter("endDate", Date.valueOf(bookingDTO.getEndDate()));

        Boolean criticalFieldChanged = (Boolean) query.getSingleResult();
        logger.info("Critical Field Changed : {} ",criticalFieldChanged);

        int userId = bookingDTO.getUserId();
        String url = "https://vehiclereservationproducer.onrender.com/users/"+userId;
        ResponseEntity<User> response  = restTemplate.getForEntity(url, User.class);
        User user = response.getBody();
        String userName = user.getName();
        String userEmail = user.getEmail();
        // If critical fields changed, send update mail to user
        if (criticalFieldChanged) {
            System.out.println("Critical Fields changed: " + bookingId);
            mailService.sendMail(userEmail, userName, "updated");
        }
        if(status.toLowerCase().equals("created")){
            mailService.sendMail(userEmail, userName, "created");
        }
        bookingService.getAllBookings();

    } catch (Exception e) {
        System.err.println("Error calling save_or_update_booking function: " + e.getMessage());
        e.printStackTrace();
        throw new RuntimeException("Failed to process booking event: " + e.getMessage(), e);
    }
}

}
