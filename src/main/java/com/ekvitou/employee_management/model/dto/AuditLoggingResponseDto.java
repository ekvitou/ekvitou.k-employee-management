package com.ekvitou.employee_management.model.dto;

import java.time.LocalDateTime;

public record AuditLoggingResponseDto (
        String entityName,
        String entityUuid,
        String action,
        String performedBy,
        LocalDateTime performedAt
) {}
