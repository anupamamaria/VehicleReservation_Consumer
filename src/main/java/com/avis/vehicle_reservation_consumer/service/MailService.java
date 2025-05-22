package com.avis.vehicle_reservation_consumer.service;

public interface MailService {
    void sendMail(String toEmail, String userName, String status);
}
