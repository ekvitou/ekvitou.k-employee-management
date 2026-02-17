package com.ekvitou.employee_management.controller;

import com.ekvitou.employee_management.model.entity.EmployeeStatus;
import com.ekvitou.employee_management.model.service.EmployeeReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/v1/reports/employees")
@RequiredArgsConstructor
public class EmployeeReportController {
    private final EmployeeReportService employeeReportService;

    @GetMapping(value = "/csv", produces = "text/csv")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<byte[]> exportCsv(
            @RequestParam(required = false) String departmentUuid,
            @RequestParam(required = false) String positionUuid,
            @RequestParam(required = false) EmployeeStatus status) {

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm"));

        String filename = "employees-" + timestamp + ".csv";

        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(employeeReportService.exportEmployeesToCsv(departmentUuid, positionUuid, status));
    }

    @GetMapping(value = "/excel", produces = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    @PreAuthorize("hasRole('ADMIN')") // Restrict export to ADMIN role
    public ResponseEntity<byte[]> exportExcel(
            @RequestParam(required = false) String departmentUuid,
            @RequestParam(required = false) String positionUuid,
            @RequestParam(required = false) EmployeeStatus status) {

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm"));

        String filename = "employees-" + timestamp + ".xlsx";

        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(employeeReportService.exportEmployeesToExcel(departmentUuid, positionUuid, status));
    }
}
