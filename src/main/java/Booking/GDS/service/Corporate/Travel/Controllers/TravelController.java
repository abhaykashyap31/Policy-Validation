package Booking.GDS.service.Corporate.Travel.Controllers;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

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
import org.springframework.web.client.HttpClientErrorException;

import Booking.GDS.service.Corporate.Travel.Dto.TravelDto;
import Booking.GDS.service.Corporate.Travel.Entities.Employee;
import Booking.GDS.service.Corporate.Travel.Entities.Travel;
import Booking.GDS.service.Corporate.Travel.Repo.EmployeeRepository;
import Booking.GDS.service.Corporate.Travel.Repo.TravelRepository;
import Booking.GDS.service.Corporate.Travel.Services.PolicyValidationClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
@Slf4j 
@RestController 
@RequestMapping("/travel")
public class TravelController {

    @Autowired 
    private TravelRepository travelRepository;

    @Autowired 
    private EmployeeRepository employeeRepository;

    @Autowired
    private PolicyValidationClient policyValidationClient;


    @CacheEvict(value = "allTravel", allEntries = true)
    @PostMapping("/book")
    public String TravelRegister(@RequestBody TravelDto travelDto){

        if (travelDto == null || travelDto.getDistance() <= 0 || travelDto.getExpense() < 0) {
//            throw new HttpClientErrorException.BadRequest(
//                    "Distance must be positive and expense cannot be negative");
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Distance must be positive and expense cannot be negative"
            );
        }

        Optional<Employee> emp = employeeRepository.findById(travelDto.getEmpId());
         if(!emp.isPresent())
        {
            log.debug("cannot find employee with id : {}",travelDto.getEmpId());
            return "Connot find employee";
        }

        Employee employee = emp.get();
        Travel t = new Travel();

        String id = UUID.randomUUID().toString().replace("-", "");

        t.setId(id);
        t.setGrade(employee.getGrade());
        t.setDate(LocalDate.parse(travelDto.getDate()));
        t.setFromLocation(travelDto.getSource());
        t.setTravelStatus("PENDING");
        t.setToLocation(travelDto.getDestination());
        t.setDistance(travelDto.getDistance());
        t.setMode(travelDto.getMode());
        t.setExpense(travelDto.getExpense());
        t.setEmpId(travelDto.getEmpId());

        travelRepository.save(t);

        /*
        Legacy policy call retained for reference. The active client below uses
        the updated policy-validation endpoint and configurable base URL.
        Map<String, Object> request = new HashMap<>();
        request.put("source", travelDto.getSource());
        request.put("destination", travelDto.getDestination());
        request.put("distance", travelDto.getDistance());
        request.put("mode", travelDto.getMode());
        request.put("expense", travelDto.getExpense());
        request.put("date", travelDto.getDate());
        request.put("employeeGrade", employee.getGrade());
        request.put("travelId", id);
        String url = "http://localhost:8081/api/validate";
        ResponseEntity<String> response =
            restTemplate.postForEntity(url, request, String.class);
        */
        Map<String, Object> validation =
            policyValidationClient.validate(travelDto, employee.getGrade(), id);
        boolean bookingValid = Boolean.TRUE.equals(validation.get("bookingValid"));
        t.setTravelStatus(bookingValid ? "APPROVED" : "REJECTED");
        travelRepository.save(t);


        log.info("saving travel for employee {}",travelDto.getEmpId());
        return bookingValid ? "Travel booking Done" : "Travel booking rejected";
    }


    @PatchMapping("/status")
    public String ToggleStatus(@RequestBody Map<String, Object> body){

        boolean status = (boolean)body.get("bookingValid");
        String id = (String)body.get("bookingId");
        // String flag = (String)body.get("bookingFlag");

        Optional<Travel> travel = travelRepository.findById(id);

        if(travel.isPresent())
        {
            Travel t = travel.get();
            
            if(status)
            t.setTravelStatus("APPROVED");
            else
            t.setTravelStatus("REJECTED");

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
    public Travel getTravel(@PathVariable ("id") String id)
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
    public String DeleteTravel(@PathVariable ("id") String id){

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