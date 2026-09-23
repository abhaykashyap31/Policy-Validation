package Booking.GDS.service.Corporate.Travel.Dto;

import java.io.Serializable;

public record TravelValidationResultEvent(
    String travelId,
    String status, // e.g. "APPROVED" or "REJECTED"
    boolean isValid,
    String message
){}
