package com.avis.vehicle_reservation_consumer.kafka;

import com.avis.vehicle_reservation_consumer.dto.BookingDTO;
import com.avis.vehicle_reservation_consumer.service.BookingService;
import com.avis.vehicle_reservation_consumer.util.EncryptionUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.PostConstruct;
import in.co.avis.avro.Booking;
import org.apache.avro.generic.GenericRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BookingKafkaConsumer {
    private final BookingService bookingService;
    private static final Logger log = LoggerFactory.getLogger(BookingKafkaConsumer.class);

    public BookingKafkaConsumer(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @KafkaListener(topics = "booking-events", groupId = "booking-group")
    public void consume(Booking message ) {
        try {
            System.out.println(message.getBookingId());



            ObjectMapper objectMapper = new ObjectMapper();
////            objectMapper.registerModule(new JavaTimeModule());
////            EncryptedBookingMessage encryptedBooking = objectMapper.readValue(message, EncryptedBookingMessage.class);
//
            UUID bookingId = UUID.fromString(message.getBookingId().toString());
            String eventType = message.getEventType().toString();
            String timestamp = message.getTimestamp().toString();
            String encryptedPayload = message.getEncryptedPayload().toString();
//
//
////            System.out.println("Received booking event:");
////            System.out.println("Booking ID: " + bookingId);
////            System.out.println("Timestamp : " + timestamp);
////            System.out.println(encryptedBooking.getEncryptedPayload());

            String decryptedJson = EncryptionUtil.decrypt(encryptedPayload);
            log.info("Decrypted JSON: {}", decryptedJson);

            if(!eventType.toLowerCase().equals("updated")){
                BookingDTO bookingDTO = objectMapper.readValue(decryptedJson, BookingDTO.class);
                bookingService.saveBooking(bookingId, timestamp, bookingDTO, eventType);
            }
            else{
                BookingDTO bookingDTO = objectMapper.readValue(decryptedJson, BookingDTO.class);
                bookingService.updateBooking(bookingId, timestamp, bookingDTO, eventType);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PostConstruct
    public void init(){
        System.out.println("Booking consumer is ready");
    }
}
