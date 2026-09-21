package com.company.travelplanner.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class ServiceClientConfiguration {

	@Bean
	@LoadBalanced
	RestClient.Builder namedServiceRestClientBuilder() {
		return RestClient.builder();
	}

	@Bean
	@org.springframework.context.annotation.Primary
	RestClient.Builder defaultRestClientBuilder() {
		return RestClient.builder();
	}
}