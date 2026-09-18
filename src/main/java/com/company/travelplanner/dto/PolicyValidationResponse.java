package com.company.travelplanner.dto;

import com.company.travelplanner.common.enums.ValidationStatus;
import java.time.LocalDateTime;
import java.util.List;

public record PolicyValidationResponse(
        Long validationId,
        Long travelRequestId,
        Long policyId,
        ValidationStatus overallStatus,
        LocalDateTime validatedAt,
        List<PolicyViolationResponse> violations) {
}
