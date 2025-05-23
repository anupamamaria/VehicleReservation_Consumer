package com.avis.vehicle_reservation_consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class VehicleReservationConsumerApplication {

	public static void main(String[] args) {
		SpringApplication.run(VehicleReservationConsumerApplication.class, args);
	}

}
