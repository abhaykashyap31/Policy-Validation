package Booking.GDS.service.Corporate.Travel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching 
public class CorporateTravelApplication {

	public static void main(String[] args) {
		SpringApplication.run(CorporateTravelApplication.class, args);
	}
	
}
