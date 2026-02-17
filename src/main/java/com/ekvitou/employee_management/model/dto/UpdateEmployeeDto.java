package com.ekvitou.employee_management.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UpdateEmployeeDto(
        @NotBlank
        String firstName,
        @NotBlank
        String lastName,
        @Email
        @NotBlank
        String email,
        String phone,
        @NotBlank
        String departmentUuid,
        @NotBlank
        String positionUuid,
        @NotBlank
        String status,
        Boolean isDeleted
) {}
