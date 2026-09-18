package com.company.travelplanner.dto;

public record EmployeeResponse(
        Long id,
        String employeeCode,
        String name,
        String email,
        String designation,
        String grade,
        Long departmentId,
        boolean active) {
}
