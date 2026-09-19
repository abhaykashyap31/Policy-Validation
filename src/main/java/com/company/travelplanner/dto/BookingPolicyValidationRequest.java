package com.company.travelplanner.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.company.travelplanner.common.enums.TravelMode;

public record BookingPolicyValidationRequest(
        String travelId,
        String source,
        String destination,
        BigDecimal distance,
        TravelMode mode,
        String employeeGrade,
        BigDecimal expense,
        LocalDate date) {
}