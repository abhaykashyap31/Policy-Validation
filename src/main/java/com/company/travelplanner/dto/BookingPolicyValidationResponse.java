package com.company.travelplanner.dto;

import com.company.travelplanner.common.enums.ValidationStatus;

public record BookingPolicyValidationResponse(
        PolicyValidationSummary policyValidation,
        String bookingId,
        String bookingFlag,
        boolean bookingValid) {

    public record PolicyValidationSummary(
            ValidationStatus overallStatus,
            boolean bookingValid,
            String bookingFlag) {
    }
}