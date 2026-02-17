package com.ekvitou.employee_management.model.repository;

import com.ekvitou.employee_management.model.entity.AuditLogging;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditLoggingRepository
        extends JpaRepository<AuditLogging, Long>, JpaSpecificationExecutor<AuditLogging> {}
