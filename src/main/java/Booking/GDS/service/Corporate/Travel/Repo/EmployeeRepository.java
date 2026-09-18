package Booking.GDS.service.Corporate.Travel.Repo;

import org.springframework.data.jpa.repository.JpaRepository;

import Booking.GDS.service.Corporate.Travel.Entities.Employee;
import java.util.*;

public interface EmployeeRepository extends JpaRepository<Employee,Integer>{

    Optional<Employee> findByEmail(String email);

}