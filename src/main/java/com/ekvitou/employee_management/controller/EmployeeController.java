package com.ekvitou.employee_management.controller;

import com.ekvitou.employee_management.model.dto.CreateEmployeeDto;
import com.ekvitou.employee_management.model.dto.UpdateEmployeeDto;
import com.ekvitou.employee_management.model.entity.EmployeeStatus;
import com.ekvitou.employee_management.model.service.EmployeeService;
import com.ekvitou.employee_management.utils.ResponseTemplate;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;
import java.util.Date;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/employees")
public class EmployeeController {
    private final EmployeeService employeeService;

    @PreAuthorize("hasAnyRole('ADMIN','HR','USER')")
    @PostMapping
    public ResponseTemplate<Object> createEmployee(@RequestBody @Valid CreateEmployeeDto createEmployeeDto) {
        return ResponseTemplate
                .builder()
                .status(HttpStatus.CREATED.toString())
                .message("Successfully created an employee!")
                .date(Date.from(Instant.now()))
                .data(employeeService.createEmployee(createEmployeeDto))
                .build();
    }

    @PreAuthorize("hasAnyRole('ADMIN','HR','USER')")
    @DeleteMapping("/{uuid}")
    public ResponseTemplate<Object> deleteEmployeeByUuid(@PathVariable String uuid) {
        return ResponseTemplate
                .builder()
                .status(HttpStatus.OK.toString())
                .message("Successfully deleted an employee!")
                .date(Date.from(Instant.now()))
                .data(employeeService.deleteEmployeeByUuid(uuid))
                .build();
    }

    @PreAuthorize("hasAnyRole('ADMIN','HR','USER')")
    @PatchMapping("/{uuid}")
    public ResponseTemplate<Object> updateEmployeeByUuid(@PathVariable String uuid, @RequestBody @Valid UpdateEmployeeDto updateEmployeeDto) {
        return ResponseTemplate
                .builder()
                .status(HttpStatus.OK.toString())
                .message("Successfully updated an employee!")
                .date(Date.from(Instant.now()))
                .data(employeeService.updateEmployeeByUuid(uuid, updateEmployeeDto))
                .build();
    }

    @PreAuthorize("hasAnyRole('ADMIN','HR','USER')")
    @GetMapping("/{uuid}")
    public ResponseTemplate<Object> getEmployeeByUuid(@PathVariable String uuid) {
        return ResponseTemplate
                .builder()
                .status(HttpStatus.OK.toString())
                .message("Successfully retrieved an employee!")
                .date(Date.from(Instant.now()))
                .data(employeeService.getEmployeeByUuid(uuid))
                .build();
    }

    @PreAuthorize("hasAnyRole('ADMIN','HR','USER')")
    @GetMapping
    public ResponseTemplate<Object> getAllEmployees(Pageable pageable) {
        return ResponseTemplate
                .builder()
                .status(HttpStatus.OK.toString())
                .message("Successfully retrieved all employees!")
                .date(Date.from(Instant.now()))
                .data(employeeService.getAllEmployees(pageable))
                .build();
    }

    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    @GetMapping("/status")
    public ResponseTemplate<Object> getEmployeesByStatus(@RequestParam EmployeeStatus status, Pageable pageable) {
        return ResponseTemplate
                .builder()
                .status(HttpStatus.OK.toString())
                .message("Successfully retrieved employees by status!")
                .date(Date.from(Instant.now()))
                .data(employeeService.getEmployeesByStatus(status, pageable))
                .build();
    }

    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    @GetMapping("/search")
    public ResponseTemplate<Object> searchEmployeesByKeyword(@RequestParam String keyword, Pageable pageable) {
        return ResponseTemplate
                .builder()
                .status(HttpStatus.OK.toString())
                .message("Successfully searched employees!")
                .date(Date.from(Instant.now()))
                .data(employeeService.searchEmployeesByKeyword(keyword, pageable))
                .build();
    }

    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    @GetMapping("/department/{departmentUuid}")
    public ResponseTemplate<Object> filterEmployeesByDepartmentUuid(@PathVariable String departmentUuid, Pageable pageable) {
        return ResponseTemplate.builder()
                .status(HttpStatus.OK.toString())
                .message("Successfully retrieved employees by department uuid!")
                .date(Date.from(Instant.now()))
                .data(employeeService.filterEmployeesByDepartmentUuid(departmentUuid, pageable))
                .build();
    }
}