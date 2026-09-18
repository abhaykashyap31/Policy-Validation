package Booking.GDS.service.Corporate.Travel.Controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice 
public class ControllerExceptionHandler {
     @ExceptionHandler(value = { TripNotFoundException.class })
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorMessage resourceNotFoundException(TripNotFoundException ex) {
        ErrorMessage message = new ErrorMessage(
                "404",
                ex.getMessage(),
                ex.getMessage());

        return message;
    }
}
