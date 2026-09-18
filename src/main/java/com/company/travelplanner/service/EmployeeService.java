package com.company.travelplanner.service;

import com.company.travelplanner.common.exception.ResourceNotFoundException;
import com.company.travelplanner.common.exception.InvalidTravelRequestException;
import com.company.travelplanner.dto.EmployeeRequest;
import com.company.travelplanner.dto.EmployeeResponse;
import com.company.travelplanner.entity.Employee;
import com.company.travelplanner.repository.EmployeeRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Transactional(readOnly = true)
    public List<EmployeeResponse> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public EmployeeResponse getEmployee(Long id) {
        return toResponse(findEmployeeOrThrow(id));
    }

    @Transactional(readOnly = true)
    public EmployeeResponse getEmployeeByCode(String employeeCode) {
        return employeeRepository.findByEmployeeCode(employeeCode)
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with code: " + employeeCode));
    }

    @Transactional
    public EmployeeResponse createEmployee(EmployeeRequest request) {
        validateRequired(request);
        ensureUniqueCodeAndEmail(request.employeeCode(), request.email(), null);

        Employee employee = new Employee();
        applyRequest(employee, request);
        employee.setActive(request.active() == null || request.active());
        return toResponse(employeeRepository.save(employee));
    }

    @Transactional
    public EmployeeResponse updateEmployee(Long id, EmployeeRequest request) {
        validateRequired(request);
        Employee employee = findEmployeeOrThrow(id);
        ensureUniqueCodeAndEmail(request.employeeCode(), request.email(), id);

        applyRequest(employee, request);
        if (request.active() != null) {
            employee.setActive(request.active());
        }
        return toResponse(employeeRepository.save(employee));
    }

    @Transactional
    public void deleteEmployee(Long id) {
        Employee employee = findEmployeeOrThrow(id);
        employeeRepository.delete(employee);
    }

    private Employee findEmployeeOrThrow(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found: " + id));
    }

    private void validateRequired(EmployeeRequest request) {
        if (request == null || isBlank(request.employeeCode()) || isBlank(request.name()) || isBlank(request.email())) {
            throw new InvalidTravelRequestException("Employee code, name, and email are required");
        }
    }

    private void ensureUniqueCodeAndEmail(String employeeCode, String email, Long currentId) {
        employeeRepository.findByEmployeeCode(employeeCode)
                .filter(existing -> currentId == null || !existing.getId().equals(currentId))
                .ifPresent(existing -> {
                    throw new InvalidTravelRequestException("Employee code already exists: " + employeeCode);
                });
        employeeRepository.findByEmail(email)
                .filter(existing -> currentId == null || !existing.getId().equals(currentId))
                .ifPresent(existing -> {
                    throw new InvalidTravelRequestException("Email already exists: " + email);
                });
    }

    private void applyRequest(Employee employee, EmployeeRequest request) {
        employee.setEmployeeCode(request.employeeCode());
        employee.setName(request.name());
        employee.setEmail(request.email());
        employee.setDesignation(request.designation());
        employee.setGrade(request.grade());
        employee.setDepartmentId(request.departmentId());
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
