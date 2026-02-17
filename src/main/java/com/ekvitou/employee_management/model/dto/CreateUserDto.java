package com.ekvitou.employee_management.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserDto(
        @NotBlank(message = "Username is required")
        @Size(max = 50, message = "Username cannot exceed 50 characters")
        String username,

        @NotBlank(message = "Role is required")
        String role,

        String employeeUuid,

        @NotBlank(message = "Email is required")
        @Email(message = "Email should be valid")
        String email
) {}
