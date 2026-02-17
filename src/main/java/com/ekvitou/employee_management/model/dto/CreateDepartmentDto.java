package com.ekvitou.employee_management.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateDepartmentDto(
        @NotBlank(message = "Department name is required")
        @Size(max = 100, message = "Department name must not exceed 100 characters")
        String name,
        @Size(max = 300, message = "Description must not exceed 300 characters")
        String description
) {}

