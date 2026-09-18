package com.company.travelplanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(excludeFilters = @ComponentScan.Filter(
		type = FilterType.REGEX,
		pattern = "com\\.company\\.travelplanner\\.travel\\..*"))
@EntityScan(basePackages = "com.company.travelplanner.entity")
@EnableJpaRepositories(basePackages = "com.company.travelplanner.repository")
public class TravelPlannerApplication {

	public static void main(String[] args) {
		SpringApplication.run(TravelPlannerApplication.class, args);
	}

}
