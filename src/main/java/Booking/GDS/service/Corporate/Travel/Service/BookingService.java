package Booking.GDS.service.Corporate.Travel.Service;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import Booking.GDS.service.Corporate.Travel.Dto.BookingResponse;

@FeignClient(name="travel-planner")
public interface BookingService {

    @PostMapping("/api/policy-validations/booking")
    BookingResponse validateBooking(@RequestBody Map<String, Object> request);
}
