package com.ekvitou.employee_management.model.dto;

import java.time.LocalDate;

public record AuditLoggingFilterDto (
        String entityName,
        String entityUuid,
        String performedBy,  // admin username
        LocalDate fromDate,
        LocalDate toDate
) {}
