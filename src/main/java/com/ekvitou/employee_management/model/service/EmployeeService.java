package com.ekvitou.employee_management.model.service;

import com.ekvitou.employee_management.model.dto.CreateEmployeeDto;
import com.ekvitou.employee_management.model.dto.EmployeeResponseDto;
import com.ekvitou.employee_management.model.dto.UpdateEmployeeDto;
import com.ekvitou.employee_management.model.entity.EmployeeStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EmployeeService {
    EmployeeResponseDto createEmployee(CreateEmployeeDto createEmployeeDto);
    String deleteEmployeeByUuid(String uuid);
    EmployeeResponseDto updateEmployeeByUuid(String uuid, UpdateEmployeeDto updateEmployeeDto);
    EmployeeResponseDto getEmployeeByUuid(String uuid);
    Page<EmployeeResponseDto> getEmployeesByStatus(EmployeeStatus employeeStatus, Pageable pageable);
    Page<EmployeeResponseDto> getAllEmployees(Pageable pageable);
    Page<EmployeeResponseDto> searchEmployeesByKeyword(String keyword, Pageable pageable);
    Page<EmployeeResponseDto> filterEmployeesByDepartmentUuid(String departmentUuid, Pageable pageable);
}