package com.avis.vehicle_reservation_consumer.controller;

import com.avis.vehicle_reservation_consumer.entity.Booking;
import com.avis.vehicle_reservation_consumer.service.BookingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;


    @RestController
    public class BookingController {
        private static final Logger logger = LoggerFactory.getLogger(BookingController.class);
        private final BookingService bookingService;
        public BookingController(BookingService bookingService) {
            this.bookingService = bookingService;
        }
//        @GetMapping("/bookings")
//        public ResponseEntity<?> getAllBookings() {
//            try{
//                logger.info("Received request to fetch all bookings");
//                List<Booking> bookings = bookingService.getAllBookings();
//                if(bookings.isEmpty()){
//                    logger.warn("No bookings");
//                    return  ResponseEntity.noContent().build();
//                }
//                else{
//                    logger.info("Returning {} bookings ",bookings.size());
//                    return ResponseEntity.ok(bookings);
//                }
//            } catch(Exception e){
//                logger.error("Error while fetching bookings",e);
//                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while fetching bookings");
//            }
//        }
        @GetMapping("/recentBookings/{userId}")
        public ResponseEntity<?> getRecentBookings(@PathVariable int userId) {
            try{
                logger.info("Received request to fetch recent bookings for userId: {}",userId);
                List<Booking> bookings = bookingService.getRecentBookings(userId);
                if(bookings.isEmpty()){
                    logger.warn("No bookings");
                    return  ResponseEntity.noContent().build();
                }
                else{
                    logger.info("Returning {} recent bookings ",bookings.size());
                    return ResponseEntity.ok(bookings);
                }
            } catch(Exception e){
                logger.error("Error while fetching recent bookings",e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while fetching bookings");
            }
        }
        @GetMapping("/bookings/source")
        public ResponseEntity<?> getBookingsBySourceLocation(@RequestParam int sourceLocationId, @RequestParam int userId){
            try{
                logger.info("Received request to fetch bookings from source location: {} for userId: {}",sourceLocationId,userId);
                List<Booking> bookings = bookingService.findBookingsBySourceLocationAndUserId(sourceLocationId, userId);
                if(bookings.isEmpty()){
                    logger.warn("No bookings from this source location");
                    return ResponseEntity.noContent().build();
                }
                else{
                    logger.info("Returning {} bookings from source location: {} for userId: {}",bookings.size(),sourceLocationId,userId);
                    return ResponseEntity.ok(bookings);
                }
            }catch(Exception e){
                logger.error("Error while fetching bookings by source location",e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while fetching bookings by source location");
            }
        }
        @GetMapping("/bookings/destination")
        public ResponseEntity<?> getBookingsByDestinationLocation(@RequestParam int destinationLocationId, @RequestParam int userId){
            try{
                logger.info("Received request to fetch bookings to destination location: {} for userId: {}",destinationLocationId, userId);
                List<Booking> bookings = bookingService.findBookingsByDestinationLocationAndUserId(destinationLocationId, userId);
                if(bookings.isEmpty()){
                    logger.warn("No bookings to this destination location");
                    return ResponseEntity.noContent().build();
                }
                else{
                    logger.info("Returning {} bookings to destination location: {} ",bookings.size(),destinationLocationId);
                    return ResponseEntity.ok(bookings);
                }
            }catch(Exception e){
                logger.error("Error while fetching bookings by destination location",e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while fetching bookings by destination location");

            }
        }
    }
