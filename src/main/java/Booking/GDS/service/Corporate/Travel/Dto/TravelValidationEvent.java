package Booking.GDS.service.Corporate.Travel.Dto;

import java.io.Serializable;

public record TravelValidationEvent(
    String travelId,
    String source,
    String destination,
    Integer distance,
    String mode,
    Integer expense,
    String date,
    String employeeGrade
) {}