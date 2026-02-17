package com.ekvitou.employee_management.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreatePositionDto (
        @NotBlank(message = "Name is required")
        @Size(max = 100, message = "Name cannot exceed 100 characters")
        String name,
        @Size(max = 50, message = "Level cannot exceed 50 characters")
        String level
) {}