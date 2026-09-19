package Booking.GDS.service.Corporate.Travel.Entities;

import java.io.Serializable;
import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@AllArgsConstructor 
@NoArgsConstructor 
@Builder 
@Entity 
public class Travel implements Serializable{

    @Id
    String id;

    Integer empId;
    LocalDate date;
    String fromLocation;
    String toLocation;
    int expense;
    String mode;
    Integer distance;
    String grade;
    
    // @Enumerated (EnumType.STRING)
    // TravelStatus Status;

    String travelStatus;
    
}
