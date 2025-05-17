//package com.avis.vehicle_reservation_consumer.kafka;
//
//import com.avis.vehicle_reservation_consumer.dto.BookingDTO;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.kafka.core.KafkaTemplate;
//import com.avis.vehicle_reservation_consumer.util.EncryptionUtil;
//import org.springframework.stereotype.Service;
//
//@Service
//public class Producer {
//    private final KafkaTemplate<String, String> kafkaTemplate;
//
//    public Producer(KafkaTemplate<String, String> kafkaTemplate) {
//        this.kafkaTemplate = kafkaTemplate;
//    }
//
////    public void sendTestBooking() {
////        try {
////            // Create a dummy booking
////            BookingDTO dto = new BookingDTO();
////            dto.setId(101);
////            dto.setUserName("John Doe");
////            dto.setEmail("john.doe@example.com");
////            dto.setVehicleType("Sedan");
////            dto.setSource("City A");
////            dto.setDestination("City B");
////
////            // Convert to JSON
////            ObjectMapper mapper = new ObjectMapper();
////            String json = mapper.writeValueAsString(dto);
////
////            // Encrypt
////            String encrypted = EncryptionUtil.encrypt(json);
////
////            // Send to Kafka
////            kafkaTemplate.send("booking-events", encrypted);
////
////            System.out.println("Encrypted booking message sent.");
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
////    }
//}
