package com.company.travelplanner.dto;

import com.company.travelplanner.common.enums.TravelMode;
import java.math.BigDecimal;

public record TravelRequestCreateRequest(
        String sourceCity,
        String destinationCity,
        BigDecimal distance,
        TravelMode travelMode,
        String employeeGrade,
        BigDecimal expense) {
}
