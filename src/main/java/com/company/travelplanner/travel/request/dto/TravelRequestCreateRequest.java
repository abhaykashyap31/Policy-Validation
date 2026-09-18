package com.company.travelplanner.travel.request.dto;

import com.company.travelplanner.common.enums.TravelClass;
import com.company.travelplanner.common.enums.TravelMode;
import java.math.BigDecimal;
import java.time.LocalDate;

public record TravelRequestCreateRequest(
        String purpose,
        String sourceCity,
        String destinationCity,
        LocalDate departureDate,
        LocalDate returnDate,
        TravelMode travelMode,
        TravelClass travelClass,
        BigDecimal estimatedCost) {
}
