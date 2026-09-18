package Booking.GDS.service.Corporate.Travel.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Booking.GDS.service.Corporate.Travel.Entities.Employee;
import Booking.GDS.service.Corporate.Travel.Entities.Travel;
import Booking.GDS.service.Corporate.Travel.Repo.TravelRepository;
import lombok.extern.slf4j.Slf4j;
import Booking.GDS.service.Corporate.Travel.Repo.EmployeeRepository;

import java.time.LocalDate;
import java.util.*;

@Slf4j 
@RestController 
@RequestMapping("/travel")
public class TravelController {

    @Autowired 
    private TravelRepository travelRepository;

    @Autowired 
    private EmployeeRepository employeeRepository;


    @CacheEvict(value = "allTravel", allEntries = true)
    @PostMapping("/book")
    public String TravelRegister(@RequestBody Map<String,Object> body){


        String source = (String)body.get("Source");
        String Destination = (String)body.get("Destination");
        int Distance = (Integer)body.get("Distance");
        String Mode = (String)body.get("Mode");
        Integer Emp_id = (Integer)body.get("Emp_id");
        int Expense = (int)body.get("Expense");
        String date = (String)body.get("Date");

        Optional<Employee> emp = employeeRepository.findById(Emp_id);

        if(!emp.isPresent())
        {
            log.debug("cannot find employee with id : {}", Emp_id);
            return "Connot find employee";
        }

        Employee employee = emp.get();

        Travel t = new Travel();

        t.setGrade(employee.getGrade());
        t.setDate(LocalDate.parse(date));
        t.setFromLocation(source);
        t.setTravelStatus("PENDING");
        t.setToLocation(Destination);
        t.setDistance(Distance);
        t.setMode(Mode);
        t.setExpense(Expense);
        t.setEmpId(Emp_id);
        
        log.info("saving travel for employee {}",Emp_id);
        travelRepository.save(t);

        return "Travel booking Done";
    }


    @PatchMapping("/status")
    public String ToggleStatus(@RequestBody Map<String, Object> body){

        String status = (String)body.get("Status");
        int id = (Integer)body.get("Id");

        Optional<Travel> travel = travelRepository.findById(id);

        if(travel.isPresent())
        {
            Travel t = travel.get();
            t.setTravelStatus(status);
            travelRepository.save(t);   
        }
        else{
            return "Cannot find travel ID";
        }

        return "Status Updated";
    }

    @Cacheable("allTravel")
    @GetMapping("/allTravel")
    public List<Travel> allTravels(){
        log.info("fecthing all Travels");
        List<Travel> t = travelRepository.findAll();
        return t;
    }

    @GetMapping("/getTravel/{id}")
    public Travel getTravel(@PathVariable ("id") int id)
    {
        log.info("fecthing travel for id : {}" ,id);
        Optional<Travel> t = travelRepository.findById(id);

        if(!t.isPresent())
        {
            throw new TripNotFoundException("Cannot find booking with this id");
        }

        return t.get();
    }

    @CacheEvict(value = "allTravel", allEntries = true)
    @DeleteMapping("/remove/{id}")
    public String DeleteTravel(@PathVariable ("id") int id){

        log.info("deleted travel for id : {}",id);
        Optional<Travel> t = travelRepository.findById(id);

        if(!t.isPresent())
        {
            log.warn("cannot find travel id : {}",id);
            throw new TripNotFoundException("Cannot find booking with this id");
        }

        travelRepository.deleteById(id);

        return "Travel deleted";
    }

    @GetMapping("/employeeTravel/{id}")
    public List<Travel> employeeAllTravels(@PathVariable ("id") int id)
    {
        log.info("fecthing all travel for employee {}",id);

        Optional<Employee> emp = employeeRepository.findById(id);
        if(!emp.isPresent())
        {
            throw new TripNotFoundException("cannot find employee");
        }
        List<Travel> t = travelRepository.findAllByEmpId(id);
        return t;
    }


}
