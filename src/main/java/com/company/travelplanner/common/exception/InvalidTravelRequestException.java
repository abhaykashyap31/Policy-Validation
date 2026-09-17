package com.company.travelplanner.common.exception;

public class InvalidTravelRequestException extends RuntimeException {

    public InvalidTravelRequestException(String message) {
        super(message);
    }
}
