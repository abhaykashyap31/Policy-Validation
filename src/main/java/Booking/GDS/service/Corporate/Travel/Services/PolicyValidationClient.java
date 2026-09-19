package Booking.GDS.service.Corporate.Travel.Services;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import Booking.GDS.service.Corporate.Travel.Dto.TravelDto;

@Service
public class PolicyValidationClient {

    private final RestTemplate restTemplate;
    private final String policyValidationUrl;

    public PolicyValidationClient(RestTemplate restTemplate,
                                  @Value("${policy.validation.url:http://localhost:8081}") String policyValidationUrl) {
        this.restTemplate = restTemplate;
        this.policyValidationUrl = policyValidationUrl;
    }

    public Map<String, Object> validate(TravelDto travelDto, String employeeGrade, String travelId) {
        Map<String, Object> request = new HashMap<>();
        request.put("travelId", travelId);
        request.put("source", travelDto.getSource());
        request.put("destination", travelDto.getDestination());
        request.put("distance", travelDto.getDistance());
        request.put("mode", travelDto.getMode().toUpperCase(Locale.ROOT));
        request.put("employeeGrade", employeeGrade);
        request.put("expense", travelDto.getExpense());
        request.put("date", travelDto.getDate());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

        return restTemplate.exchange(
            policyValidationUrl + "/api/policy-validations/booking",
            HttpMethod.POST,
            entity,
            new ParameterizedTypeReference<Map<String, Object>>() {})
            .getBody();
    }

        /*
        The RestClient implementation is retained for reference:

        private final RestClient restClient;

        this.restClient = restClientBuilder.baseUrl(policyValidationUrl).build();

        return restClient.post()
            .uri("/api/policy-validations/booking")
            .contentType(MediaType.APPLICATION_JSON)
            .body(request)
            .retrieve()
            .body(new ParameterizedTypeReference<Map<String, Object>>() {});
        */
}