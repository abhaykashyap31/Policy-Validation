package Booking.GDS.service.Corporate.Travel.Repo;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import Booking.GDS.service.Corporate.Travel.Entities.Travel;

public interface TravelRepository extends JpaRepository<Travel,String>{

    List<Travel> findAllByEmpId(Integer empId);
    
}