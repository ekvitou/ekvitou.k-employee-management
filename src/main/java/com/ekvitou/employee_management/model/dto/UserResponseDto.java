package com.ekvitou.employee_management.model.dto;

import java.time.LocalDateTime;

public record UserResponseDto(
        String uuid,
        String keycloakId,
        String username,
        String email,
        String role,
        LocalDateTime createdAt, // first record created in system
        LocalDateTime updatedAt, // last record updated in system
        String createdBy, // who created this user
        String updatedBy // who updated this user
) {}