package Booking.GDS.service.Corporate.Travel.Controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

@RestControllerAdvice 
public class ControllerExceptionHandler {
     @ExceptionHandler(value = { HttpClientErrorException.BadRequest.class })
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMessage badRequestException(HttpClientErrorException.BadRequest ex) {
        ErrorMessage message = new ErrorMessage(
                "400",
                "Invalid booking request",
                ex.getMessage());

        return message;
    }

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
