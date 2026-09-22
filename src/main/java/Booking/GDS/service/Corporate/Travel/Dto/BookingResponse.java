package Booking.GDS.service.Corporate.Travel.Dto;

public record BookingResponse(
        String bookingId,
        String bookingFlag,
        boolean bookingValid
) {}
