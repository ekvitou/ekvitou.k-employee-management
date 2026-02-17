package com.ekvitou.employee_management.model.service;

import com.ekvitou.employee_management.exception.*;
import com.ekvitou.employee_management.mapper.EmployeeMapstruct;
import com.ekvitou.employee_management.model.dto.CreateEmployeeDto;
import com.ekvitou.employee_management.model.dto.EmployeeResponseDto;
import com.ekvitou.employee_management.model.dto.UpdateEmployeeDto;
import com.ekvitou.employee_management.model.entity.Department;
import com.ekvitou.employee_management.model.entity.Employee;
import com.ekvitou.employee_management.model.entity.EmployeeStatus;
import com.ekvitou.employee_management.model.entity.Position;
import com.ekvitou.employee_management.model.repository.DepartmentRepository;
import com.ekvitou.employee_management.model.repository.EmployeeRepository;
import com.ekvitou.employee_management.model.repository.PositionRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final PositionRepository positionRepository;
    private final EmployeeMapstruct employeeMapstruct;

    @Transactional
    @Override
    public EmployeeResponseDto createEmployee(CreateEmployeeDto createDto) {
        if (employeeRepository.existsByEmail(createDto.email())) {
            throw new DuplicateEmployeeException("Email already exists");
        }
        Employee employee = new Employee();
        employee.setFirstName(createDto.firstName());
        employee.setLastName(createDto.lastName());
        employee.setEmail(createDto.email());
        employee.setPhone(createDto.phone());
        employee.setStatus(EmployeeStatus.ACTIVE);
        employee.setIsDeleted(false);
        Department department = departmentRepository.findDepartmentByUuid(createDto.departmentUuid())
                .orElseThrow(() -> new DepartmentNotFoundException("Department not found"));
        employee.setDepartment(department);
        Position position = positionRepository.findPositionByUuid(createDto.positionUuid())
                .orElseThrow(() -> new PositionNotFoundException("Position not found"));
        employee.setPosition(position);
        employeeRepository.save(employee);
        return employeeMapstruct.mapFromEmployeeToEmployeeResponseDto(employee);
    }

    @Transactional
    @Override
    public String deleteEmployeeByUuid(String uuid) {
        Employee employee = employeeRepository.findEmployeeByUuid(uuid)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found"));
        if (employee.getIsDeleted()) {
            throw new EmployeeAlreadyDeletedException("Employee is already deleted");
        }
        employee.setIsDeleted(true);
        employeeRepository.save(employee);
        return employee.getUuid();
    }

    @Transactional
    @Override
    public EmployeeResponseDto updateEmployeeByUuid(String uuid, UpdateEmployeeDto updateDto) {
        Employee employee = employeeRepository.findEmployeeByUuid(uuid)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found"));
        employee.setFirstName(updateDto.firstName());
        employee.setLastName(updateDto.lastName());
        employee.setEmail(updateDto.email());
        employee.setPhone(updateDto.phone());

        try {
            employee.setStatus(EmployeeStatus.valueOf(updateDto.status().toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new InvalidEmployeeStatusException("Invalid employee status: " + updateDto.status());
        }
        employee.setIsDeleted(updateDto.isDeleted());

        Department department = departmentRepository.findDepartmentByUuid(updateDto.departmentUuid())
                .orElseThrow(() -> new DepartmentNotFoundException("Department not found"));
        employee.setDepartment(department);
        Position position = positionRepository.findPositionByUuid(updateDto.positionUuid())
                .orElseThrow(() -> new PositionNotFoundException("Position not found"));
        employee.setPosition(position);
        employeeRepository.save(employee);
        return employeeMapstruct.mapFromEmployeeToEmployeeResponseDto(employee);
    }

    @Transactional(readOnly = true)
    @Override
    public EmployeeResponseDto getEmployeeByUuid(String uuid) {
        Employee employee = employeeRepository.findEmployeeByUuid(uuid)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found"));
        return employeeMapstruct.mapFromEmployeeToEmployeeResponseDto(employee);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<EmployeeResponseDto> getEmployeesByStatus(EmployeeStatus status, Pageable pageable) {
        if (status != EmployeeStatus.ACTIVE && status != EmployeeStatus.INACTIVE) {
            throw new EmployeeNotFoundException("Invalid employee status");
        }
        Page<Employee> employees = employeeRepository.findEmployeesByStatusAndIsDeletedIsFalse(status, pageable);
        if (employees.isEmpty()) {
            throw new EmployeeNotFoundException("No employees found with status: " + status);
        }
        return employees.map(employeeMapstruct::mapFromEmployeeToEmployeeResponseDto);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<EmployeeResponseDto> getAllEmployees(Pageable pageable) {
        Page<Employee> employees = employeeRepository.findEmployeesByIsDeletedIsFalse(pageable);
        return employees.map(employeeMapstruct::mapFromEmployeeToEmployeeResponseDto);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<EmployeeResponseDto> searchEmployeesByKeyword(String keyword, Pageable pageable) {
        if (keyword == null || keyword.isBlank()) {
            throw new EmployeeNotFoundException("Keyword must not be empty");
        }
        Page<Employee> employees = employeeRepository.searchEmployees(keyword, pageable);
        if (employees.isEmpty()) {
            throw new EmployeeNotFoundException("No employees found for keyword: " + keyword);
        }
        return employees.map(employeeMapstruct::mapFromEmployeeToEmployeeResponseDto);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<EmployeeResponseDto> filterEmployeesByDepartmentUuid(String departmentUuid, Pageable pageable) {
        if (departmentUuid == null || departmentUuid.isBlank()) {
            throw new EmployeeNotFoundException("Department UUID must not be empty");
        }
        Page<Employee> employees = employeeRepository.findEmployeesByDepartment_UuidAndIsDeletedIsFalse(departmentUuid, pageable);
        if (employees.isEmpty()) {
            throw new EmployeeNotFoundException("No employees found for department UUID: " + departmentUuid);
        }
        return employees.map(employeeMapstruct::mapFromEmployeeToEmployeeResponseDto);
    }
}
