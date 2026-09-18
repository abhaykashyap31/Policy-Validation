package com.company.travelplanner.dto;

import com.company.travelplanner.common.enums.Severity;

public record PolicyViolationResponse(
        String ruleCode,
        String message,
        String actualValue,
        String allowedValue,
        Severity severity) {
}
