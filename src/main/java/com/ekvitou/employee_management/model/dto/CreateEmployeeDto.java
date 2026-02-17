package com.ekvitou.employee_management.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateEmployeeDto(
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
        String positionUuid
) {}
