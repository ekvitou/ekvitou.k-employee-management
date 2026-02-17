package com.ekvitou.employee_management.controller;

import com.ekvitou.employee_management.model.dto.AuditLoggingFilterDto;
import com.ekvitou.employee_management.model.service.AuditLoggingService;
import com.ekvitou.employee_management.utils.ResponseTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.time.Instant;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/audit-logs")
public class AuditLoggingController {
    private final AuditLoggingService auditLoggingService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseTemplate<Object> getAuditLogs(
            @RequestParam(required = false) String entityName,
            @RequestParam(required = false) String entityUuid,
            @RequestParam(required = false) String performedBy,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            Pageable pageable
    ) {
        LocalDateTime fromDateTime = fromDate != null ? fromDate.atStartOfDay() : null;
        LocalDateTime toDateTime = toDate != null ? toDate.atTime(23, 59, 59) : null;

        AuditLoggingFilterDto filterDto = new AuditLoggingFilterDto(
                entityName != null ? entityName.toLowerCase() : null,
                entityUuid,
                performedBy,
                fromDate,
                toDate
        );

        // Call service with date-time converted to cover full day
        return ResponseTemplate
                .builder()
                .status(HttpStatus.OK.toString())
                .message("Successfully retrieved audit logs!")
                .date(Date.from(Instant.now()))
                .data(auditLoggingService.getAuditLogs(filterDto, pageable))
                .build();
    }
}