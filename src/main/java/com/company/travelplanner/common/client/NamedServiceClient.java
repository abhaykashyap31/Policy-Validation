package com.company.travelplanner.common.client;

import java.util.Objects;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.client.RestClient;

/**
 * Base client for calling another registered service by Eureka application name.
 * The URI must use a service name, for example: http://policy-service/api/policies.
 */
@Component
public class NamedServiceClient {

	private final RestClient.Builder restClientBuilder;

	public NamedServiceClient(@Qualifier("namedServiceRestClientBuilder") RestClient.Builder restClientBuilder) {
		this.restClientBuilder = restClientBuilder;
	}

	public <T> T get(String serviceName, String path, Class<T> responseType) {
		return client(serviceName).get().uri(path).retrieve().body(responseType);
	}

	public <T, R> T post(String serviceName, String path, R request, Class<T> responseType) {
		return client(serviceName).post().uri(path).body(request).retrieve().body(responseType);
	}

	private RestClient client(String serviceName) {
		String normalizedServiceName = Objects.requireNonNull(serviceName, "serviceName").trim();
		if (normalizedServiceName.isEmpty() || normalizedServiceName.contains("/")
				|| normalizedServiceName.contains(":")) {
			throw new IllegalArgumentException("serviceName must be an Eureka application name");
		}
		return restClientBuilder.baseUrl("http://" + normalizedServiceName).build();
	}
}