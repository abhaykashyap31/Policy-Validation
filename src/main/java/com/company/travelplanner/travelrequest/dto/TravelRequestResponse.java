package com.company.travelplanner.travelrequest.dto;

import com.company.travelplanner.common.enums.TravelClass;
import com.company.travelplanner.common.enums.TravelRequestStatus;
import java.math.BigDecimal;
import java.time.LocalDate;

public record TravelRequestResponse(
        Long id,
        Long employeeId,
        String purpose,
        String sourceCity,
        String destinationCity,
        LocalDate departureDate,
        LocalDate returnDate,
        TravelClass travelClass,
        BigDecimal estimatedCost,
        TravelRequestStatus status) {
}
