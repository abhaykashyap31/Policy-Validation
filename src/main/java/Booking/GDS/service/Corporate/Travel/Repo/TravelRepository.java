package Booking.GDS.service.Corporate.Travel.Repo;
import org.springframework.data.jpa.repository.JpaRepository;
import Booking.GDS.service.Corporate.Travel.Entities.Travel;
import java.util.*;

public interface TravelRepository extends JpaRepository<Travel,Integer>{

    List<Travel> findAllByEmpId(Integer empId);
    
}