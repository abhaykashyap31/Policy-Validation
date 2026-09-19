package Booking.GDS.service.Corporate.Travel.Dto;

import lombok.Data;

@Data 
public class TravelDto {

    private String source;
    private String destination;
    private int distance;
    private String mode;
    private Integer empId;
    private int expense;
    private String date;
}
