package com.avis.vehicle_reservation_consumer.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingDTO {

    // Decrypted fields from BookingRequestDto
    private Integer userId;
    private Integer carId;
    private Integer sourceLocationId;
    private Integer destinationLocationId;
    private LocalDate startDate;
    private LocalDate endDate;
}
