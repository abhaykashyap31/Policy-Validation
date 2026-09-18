package Booking.GDS.service.Corporate.Travel.Entities;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data 
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity 
public class Employee implements Serializable {
    
    @Id 
    @GeneratedValue (strategy = GenerationType.SEQUENCE)
    Integer id;

    String name;
    String department;
    String email;
    String password;
    String grade; // A B C...

}
