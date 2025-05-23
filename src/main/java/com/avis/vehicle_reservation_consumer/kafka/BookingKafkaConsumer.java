package com.avis.vehicle_reservation_consumer.kafka;

import com.avis.vehicle_reservation_consumer.dto.BookingDTO;
import com.avis.vehicle_reservation_consumer.service.BookingService;
import com.avis.vehicle_reservation_consumer.util.EncryptionUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import in.co.avis.avro.Booking;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BookingKafkaConsumer {
    private final BookingService bookingService;
    private final ObjectMapper objectMapper;
    private static final Logger log = LoggerFactory.getLogger(BookingKafkaConsumer.class);

    public BookingKafkaConsumer(BookingService bookingService, ObjectMapper objectMapper) {
        this.bookingService = bookingService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "booking-events", groupId = "booking-group")
    public void consume(Booking message ) {
        try {
            UUID bookingId = UUID.fromString(message.getBookingId().toString());
            String eventType = message.getEventType().toString();
            String timestamp = message.getTimestamp().toString();
            String encryptedPayload = message.getEncryptedPayload().toString();

            log.info("Received booking event:");
            log.info("Booking ID: {}",bookingId);
            log.info("Timestamp: {}",timestamp);

            // Decrypt payload
            String decryptedJson = "";
            try{
                decryptedJson = EncryptionUtil.decrypt(encryptedPayload);
                log.info("Decrypted JSON: {}", decryptedJson);
            } catch(Exception e){
                log.error("Failed to decrypt payload: {}",e.getMessage(),e);
                return;
            }

            BookingDTO bookingDTO = objectMapper.readValue(decryptedJson, BookingDTO.class);
            bookingService.processBookingEvent(bookingId,timestamp,bookingDTO);

            // Deserialize and save/update booking
//            try {
//                if(!eventType.toLowerCase().equals("updated")){
//                    BookingDTO bookingDTO = objectMapper.readValue(decryptedJson, BookingDTO.class);
//                    bookingService.saveBooking(bookingId, timestamp, bookingDTO, eventType);
//                }
//                else{
//                    BookingDTO bookingDTO = objectMapper.readValue(decryptedJson, BookingDTO.class);
//                    bookingService.updateBooking(bookingId, timestamp, bookingDTO, eventType);
//                }
//            }catch (Exception e){
//                log.error("Failed to parse or persist booking: {}",e.getMessage(),e);
//            }

        } catch (Exception e) {
            log.error("Unexpected error while consuming booking event: {}", e.getMessage(), e);
        }
    }

    @PostConstruct
    public void init(){
        System.out.println("Booking consumer is ready");
    }
}
