package com.avis.vehicle_reservation_consumer.controller;

import com.avis.vehicle_reservation_consumer.entity.Booking;
//import com.avis.vehicle_reservation_consumer.kafka.Producer;
import com.avis.vehicle_reservation_consumer.service.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
    public class BookingController {

//        private final Producer producer;
        private final BookingService bookingService;

        public BookingController(BookingService bookingService) {
            this.bookingService = bookingService;
        }

//        @GetMapping("/send")
//        public ResponseEntity<String> send() {
//            producer.sendTestBooking();
//            return ResponseEntity.ok("Booking sent to Kafka.");
//        }

    @GetMapping("/bookings")
    public List<Booking> getAllBookings() {
        return bookingService.getAllBookings();
    }


}
