package com.company.travelplanner.dto;

import com.company.travelplanner.common.enums.TravelMode;
import java.math.BigDecimal;

public record TravelRequestResponse(
        Long travelRequestId,
        String sourceCity,
        String destinationCity,
        BigDecimal distance,
        TravelMode mode,
        String employeeGrade,
        BigDecimal expense,
        Long bookingId,
        BigDecimal expectedExpense,
        char flag,
        boolean valid) {
}
