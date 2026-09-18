package Booking.GDS.service.Corporate.Travel.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import Booking.GDS.service.Corporate.Travel.Entities.Employee;
import Booking.GDS.service.Corporate.Travel.Repo.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;


@Slf4j 
@RestController 
@RequestMapping("/auth")
public class AuthController {
    
    @Autowired 
    private EmployeeRepository employeeRepository;

    @PostMapping("/register")
    public String Register(@RequestBody Employee employee){

        log.info("Registering User :{}",employee.getName());
        Optional<Employee> emp = employeeRepository.findByEmail(employee.getEmail());
        
        if(emp.isPresent())
        {
            return "Email is already in use";
        }

        employeeRepository.save(employee);

        return "Registered";
    } 

    @GetMapping("/employee")
    public List<Employee> AllEmployees(){
        log.debug("fetching all employee ");
        return employeeRepository.findAll();
    }

    @DeleteMapping ("/employee/{id}")
    public String DeleteEmployeePathVariable(@PathVariable ("id") int id){

        log.info("deleting employee {}",id);
        Optional<Employee> employee = employeeRepository.findById(id);

        if(employee.isPresent())
        {
            employeeRepository.deleteById(id);
        }
        else{
            log.info("cannot find employee id {}",id);
            return "Connot find employee";
        }

        return "deleted";
    }

    @DeleteMapping ("/employee")
    public String DeleteEmployeeRequestParams(@RequestParam  ("id") int id){
        Optional<Employee> employee = employeeRepository.findById(id);
        log.info("deleting employee {}",id);
        if(employee.isPresent())
        {
            employeeRepository.deleteById(id);
        }
        else{
             log.info("cannot find employee id {}",id);
            return "Cannot find employee";
        }

        return "deleted";
    }
}
