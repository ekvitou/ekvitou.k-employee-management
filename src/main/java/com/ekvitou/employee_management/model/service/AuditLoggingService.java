package com.ekvitou.employee_management.model.service;

import com.ekvitou.employee_management.model.dto.AuditLoggingFilterDto;
import com.ekvitou.employee_management.model.dto.AuditLoggingResponseDto;
import com.ekvitou.employee_management.model.entity.AuditLogging;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AuditLoggingService {
    void logAction(
            String entityName,
            String entityUuid,
            AuditLogging.Action action,
            String performedBy
    );
    Page<AuditLoggingResponseDto> getAuditLogs(AuditLoggingFilterDto filterDto, Pageable pageable);
}
