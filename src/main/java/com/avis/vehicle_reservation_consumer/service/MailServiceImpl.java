package com.avis.vehicle_reservation_consumer.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.*;

import java.util.UUID;

@Service
public class MailServiceImpl implements MailService{
    //to send notification email for create and update bookings

    private final SesClient sesClient;
    private static final Logger logger = LoggerFactory.getLogger(MailServiceImpl.class);

    public MailServiceImpl(SesClient sesClient) {
        this.sesClient = sesClient;
    }

    @Value("${aws.ses.from.email}")
    private String fromEmail;

    @Override
    public void sendMail(String toEmail, String userName, String status, UUID bookingId){
        String subject;
        String bodyText;
        String bodyHtml;

        if ("updated".equalsIgnoreCase(status)) {
            subject = "Booking Updated";
            bodyText = "Hi " + userName + ",\nYour booking has been successfully updated.";
            bodyHtml = "<p>Hi <strong>" + userName + "</strong>,</p>" +
                    "<p>Your booking with ID <strong>"+bookingId +"</strong> has been <strong>successfully updated</strong>.</p>";
        } else {
            subject = "Booking Created";
            bodyText = "Hi " + userName + ",\nYour booking has been successfully created.";
            bodyHtml = "<p>Hi <strong>" + userName + "</strong>,</p>" +
                    "<p>Your booking has been <strong>successfully created</strong>. " +
                    "Your booking ID is <strong>" + bookingId + "</strong>.</p>";
        }

        try {
            Content subjectContent = Content.builder()
                    .data(subject)
                    .charset("UTF-8")
                    .build();

            Content textContent = Content.builder()
                    .data(bodyText)
                    .charset("UTF-8")
                    .build();

            Content htmlContent = Content.builder()
                    .data(bodyHtml)
                    .charset("UTF-8")
                    .build();

            Body body = Body.builder()
                    .text(textContent)
                    .html(htmlContent)
                    .build();

            Message message = Message.builder()
                    .subject(subjectContent)
                    .body(body)
                    .build();

            SendEmailRequest emailRequest = SendEmailRequest.builder()
                    .source(fromEmail)
                    .destination(Destination.builder().toAddresses(toEmail).build())
                    .message(message)
                    .build();

            sesClient.sendEmail(emailRequest);
            logger.info("Email sent to {}",toEmail);
        } catch (SesException e) {
            logger.error("Failed to send email: {}", e.awsErrorDetails().errorMessage());
        }
    }
}
