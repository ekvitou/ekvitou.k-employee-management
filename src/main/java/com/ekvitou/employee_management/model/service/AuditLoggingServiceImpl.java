package com.ekvitou.employee_management.model.service;

import com.ekvitou.employee_management.model.dto.AuditLoggingFilterDto;
import com.ekvitou.employee_management.model.dto.AuditLoggingResponseDto;
import com.ekvitou.employee_management.model.entity.AuditLogging;
import com.ekvitou.employee_management.model.repository.AuditLoggingRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditLoggingServiceImpl implements AuditLoggingService {
    private final AuditLoggingRepository auditLoggingRepository;

    @Override
    public void logAction(
            String entityName,
            String entityUuid,
            AuditLogging.Action action,
            String performedBy
    ) {
        if (entityName == null || entityUuid == null || action == null || performedBy == null) {
            throw new IllegalArgumentException("Audit logging parameters cannot be null");
        }
        AuditLogging log = new AuditLogging();
        log.setEntityName(entityName.toLowerCase());
        log.setEntityUuid(entityUuid);
        log.setAction(action);
        log.setPerformedBy(performedBy);
        log.setPerformedAt(LocalDateTime.now());
        auditLoggingRepository.save(log);
    }

    @Override
    public Page<AuditLoggingResponseDto> getAuditLogs(AuditLoggingFilterDto filterDto, Pageable pageable) {
        Specification<AuditLogging> specification = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filterDto.entityName() != null && !filterDto.entityName().isBlank()) {
                predicates.add(cb.equal(root.get("entityName"), filterDto.entityName().toLowerCase()));
            }
            if (filterDto.entityUuid() != null && !filterDto.entityUuid().isBlank()) {
                predicates.add(cb.equal(root.get("entityUuid"), filterDto.entityUuid()));
            }
            if (filterDto.performedBy() != null && !filterDto.performedBy().isBlank()) {
                predicates.add(cb.equal(root.get("performedBy"), filterDto.performedBy()));
            }
            if (filterDto.fromDate() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("performedAt"), filterDto.fromDate().atStartOfDay()));
            }
            if (filterDto.toDate() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("performedAt"), filterDto.toDate().atTime(LocalTime.MAX)));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return auditLoggingRepository.findAll(specification, pageable)
                .map(this::mapToResponseDto);
    }

    private AuditLoggingResponseDto mapToResponseDto(AuditLogging log) {
        return new AuditLoggingResponseDto(
                log.getEntityName(),
                log.getEntityUuid(),
                log.getAction().name(),
                log.getPerformedBy(),
                log.getPerformedAt()
        );
    }
}
