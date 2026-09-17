package com.company.travelplanner.employee.service;

import com.company.travelplanner.common.exception.ResourceNotFoundException;
import com.company.travelplanner.common.exception.InvalidTravelRequestException;
import com.company.travelplanner.employee.dto.EmployeeRequest;
import com.company.travelplanner.employee.dto.EmployeeResponse;
import com.company.travelplanner.employee.entity.Employee;
import com.company.travelplanner.employee.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public EmployeeResponse getEmployee(Long id) {
        return employeeRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found: " + id));
    }

    public EmployeeResponse createEmployee(EmployeeRequest request) {
        if (request == null || isBlank(request.employeeCode()) || isBlank(request.name()) || isBlank(request.email())) {
            throw new InvalidTravelRequestException("Employee code, name, and email are required");
        }
        Employee employee = new Employee();
        employee.setEmployeeCode(request.employeeCode());
        employee.setName(request.name());
        employee.setEmail(request.email());
        employee.setDesignation(request.designation());
        employee.setGrade(request.grade());
        employee.setDepartmentId(request.departmentId());
        employee.setActive(request.active() == null || request.active());
        return toResponse(employeeRepository.save(employee));
    }

    private EmployeeResponse toResponse(Employee employee) {
        return new EmployeeResponse(
                employee.getId(),
                employee.getEmployeeCode(),
                employee.getName(),
                employee.getEmail(),
                employee.getDesignation(),
                employee.getGrade(),
                employee.getDepartmentId(),
                employee.isActive());
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
