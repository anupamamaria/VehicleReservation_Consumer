package com.avis.vehicle_reservation_consumer.dto;


import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;


public class BookingDTO {

    // Decrypted fields from BookingRequestDto
    private Integer userId;
    private Integer carId;
    private Integer sourceLocationId;
    private Integer destinationLocationId;
    private LocalDate startDate;
    private LocalDate endDate;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getCarId() {
        return carId;
    }

    public void setCarId(Integer carId) {
        this.carId = carId;
    }

    public Integer getSourceLocationId() {
        return sourceLocationId;
    }

    public void setSourceLocationId(Integer sourceLocationId) {
        this.sourceLocationId = sourceLocationId;
    }

    public Integer getDestinationLocationId() {
        return destinationLocationId;
    }

    public void setDestinationLocationId(Integer destinationLocationId) {
        this.destinationLocationId = destinationLocationId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "BookingDTO{" +
                "userId=" + userId +
                ", carId=" + carId +
                ", sourceLocationId=" + sourceLocationId +
                ", destinationLocationId=" + destinationLocationId +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }

}
