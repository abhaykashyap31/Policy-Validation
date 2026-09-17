package com.company.travelplanner.employee.dto;

public record EmployeeRequest(
        String employeeCode,
        String name,
        String email,
        String designation,
        String grade,
        Long departmentId,
        Boolean active) {
}
