package com.ekvitou.employee_management.model.dto;

import java.time.LocalDateTime;

public record EmployeeResponseDto(
        String uuid,
        String firstName,
        String lastName,
        String email,
        String phone,
        String status,
        Boolean isDeleted,
        DepartmentResponseDto department_uuid,
        PositionResponseDto position_uuid,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String createdBy,
        String updatedBy
) {}