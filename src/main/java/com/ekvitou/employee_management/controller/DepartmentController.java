package com.ekvitou.employee_management.controller;

import com.ekvitou.employee_management.model.dto.CreateDepartmentDto;
import com.ekvitou.employee_management.model.dto.UpdateDepartmentDto;
import com.ekvitou.employee_management.model.service.DepartmentService;
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
@RequestMapping("/api/v1/departments")
public class DepartmentController {
    private final DepartmentService departmentService;

    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    @PostMapping
    public ResponseTemplate<Object> createDepartment(@RequestBody @Valid CreateDepartmentDto createDepartmentDto) {
        return ResponseTemplate
                .builder()
                .status(HttpStatus.CREATED.toString())
                .message("Successfully created a department!")
                .date(Date.from(Instant.now()))
                .data(departmentService.createDepartment(createDepartmentDto))
                .build();
    }

    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    @DeleteMapping("/{uuid}")
    public ResponseTemplate<Object> deleteDepartmentByUuid(@PathVariable String uuid) {
        return ResponseTemplate
                .builder()
                .status(HttpStatus.OK.toString())
                .message("Successfully deleted a department!")
                .date(Date.from(Instant.now()))
                .data(departmentService.deleteDepartmentByUuid(uuid))
                .build();
    }

    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    @PatchMapping("/{uuid}")
    public ResponseTemplate<Object> updateDepartmentByUuid(@PathVariable String uuid, @RequestBody @Valid UpdateDepartmentDto updateDepartmentDto) {
        return ResponseTemplate
                .builder()
                .status(HttpStatus.OK.toString())
                .message("Successfully updated a department!")
                .date(Date.from(Instant.now()))
                .data(departmentService.updateDepartmentByUuid(uuid, updateDepartmentDto))
                .build();
    }

    @PreAuthorize("hasAnyRole('ADMIN','HR','USER')")
    @GetMapping("/{uuid}")
    public ResponseTemplate<Object> getDepartmentByUuid(@PathVariable String uuid) {
        return ResponseTemplate
                .builder()
                .status(HttpStatus.OK.toString())
                .message("Successfully retrieved a department!")
                .date(Date.from(Instant.now()))
                .data(departmentService.getDepartmentByUuid(uuid))
                .build();
    }

    @PreAuthorize("hasAnyRole('ADMIN','HR','USER')")
    @GetMapping
    public ResponseTemplate<Object> getAllDepartments(Pageable pageable) {
        return ResponseTemplate
                .builder()
                .status(HttpStatus.OK.toString())
                .message("Successfully retrieved all departments!")
                .date(Date.from(Instant.now()))
                .data(departmentService.getAllDepartments(pageable))
                .build();
    }
}
