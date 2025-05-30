package com.avis.vehicle_reservation_consumer.service;

import java.util.UUID;

public interface MailService {
    void sendMail(String toEmail, String userName, String status, UUID bookingId);
}
