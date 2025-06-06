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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.sql.Date;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
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

    public BookingServiceImpl(BookingRepository bookingRepository,MailService mailService,
                              RestTemplate restTemplate){
        this.bookingRepository = bookingRepository;
        this.mailService = mailService;
        this.restTemplate = restTemplate;
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
    @Cacheable(cacheNames = "allBookings", key="#userId")
    public List<Booking> getRecentBookings(int userId) {
        List<Booking> allBookings = bookingRepository.findByUserId(userId);
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);
        return allBookings.stream()
                .filter(booking -> {
                    try {
                        Instant instant = Instant.parse(booking.getTimestamp());
                        LocalDateTime bookingTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
                        logger.info("booking time: {}",bookingTime);
                        return bookingTime.isAfter(oneWeekAgo);
                    } catch (Exception e) {
                        return false;
                    }
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<Booking> findBookingsBySourceLocationAndUserId(int sourceLocationId,int userId){
        return bookingRepository.findBySourceLocationIdAndUserId(sourceLocationId, userId);
    }

    @Override
    public List<Booking> findBookingsByDestinationLocationAndUserId(int destinationLocationId, int userId){
        return bookingRepository.findByDestinationLocationIdAndUserId(destinationLocationId, userId);
    }

    @Override
    @CacheEvict(value = "allBookings", allEntries = true)
    public void deleteBookingById(UUID bookingId){
        logger.info("Evicting cache: allBookings");
        Optional<Booking> optionalBooking = bookingRepository.findByBookingId(bookingId);
        if(optionalBooking.isPresent()){
            Booking booking = optionalBooking.get();
            bookingRepository.delete(booking);
            logger.info("Deleted booking with booking id: {}",bookingId);
        }
       else{
           logger.error("Booking with booking id: {} is not found",bookingId);
        }
    }

    @Override
    @CacheEvict(value = "allBookings", allEntries = true)
    public void processBookingEvent(UUID bookingId, String timestamp, BookingDTO bookingDTO) {
        logger.info("Evicting cache: allBookings");
        validateBooking(bookingId,bookingDTO);
        String status = bookingRepository.findByBookingId(bookingId).isPresent() ? "updated" : "created";
    try {
        Boolean criticalFieldChanged = bookingRepository.callSaveOrUpdateBooking(
                bookingId,
                timestamp,
                bookingDTO.getUserId(),
                bookingDTO.getCarId(),
                bookingDTO.getSourceLocationId(),
                bookingDTO.getDestinationLocationId(),
                bookingDTO.getStartDate(),
                bookingDTO.getEndDate());
        logger.info("Critical Field Changed : {} ",criticalFieldChanged);

        int userId = bookingDTO.getUserId();
        String url = "https://vehiclereservationproducer.onrender.com/users/"+userId;
        ResponseEntity<User> response  = restTemplate.getForEntity(url, User.class);
        User user = response.getBody();
        String userName = user.getName();
        String userEmail = user.getEmail();
        // If critical fields changed, send update mail to user
        if (criticalFieldChanged) {
            logger.info("Critical Fields changed for bookingId : {}",bookingId);
            mailService.sendMail(userEmail, userName, "updated",bookingId);
        }
        // If new booking is created, send create mail to user
        if(status.toLowerCase().equals("created")){
            mailService.sendMail(userEmail, userName, "created", bookingId);
        }

    } catch (Exception e) {
        logger.error("Error calling save_or_update_booking function: {}", e.getMessage());
        e.printStackTrace();
        throw new RuntimeException("Failed to process booking event: " + e.getMessage(), e);
    }
}
}
