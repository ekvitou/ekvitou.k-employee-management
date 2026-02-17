package com.ekvitou.employee_management.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdatePositionDto(
        @NotBlank(message = "Position name is required")
        @Size(max = 100, message = "Position name must be at most 100 characters")
        String name,
        @Size(max = 50, message = "Level must be at most 50 characters")
        String level
) {}