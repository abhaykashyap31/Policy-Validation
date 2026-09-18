package Booking.GDS.service.Corporate.Travel.Controllers;

import java.time.LocalDateTime;

public class ErrorMessage {
    String status;
    LocalDateTime localDateTime;
    String message;
    String description;
    public ErrorMessage(String status, String message, String description) {
        this.status = status;
        this.localDateTime = LocalDateTime.now();
        this.message = message;
        this.description = description;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }
    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    

    

}
