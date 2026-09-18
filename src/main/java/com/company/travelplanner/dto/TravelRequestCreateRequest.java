package com.company.travelplanner.dto;

import com.company.travelplanner.common.enums.TravelClass;
import java.math.BigDecimal;
import java.time.LocalDate;

public record TravelRequestCreateRequest(
        String purpose,
        String sourceCity,
        String destinationCity,
        LocalDate departureDate,
        LocalDate returnDate,
        TravelClass travelClass,
        BigDecimal estimatedCost) {
}
