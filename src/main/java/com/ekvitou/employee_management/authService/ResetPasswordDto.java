package com.ekvitou.employee_management.authService;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ResetPasswordDto(
        @Email
        @NotBlank
        String email,
        @NotBlank
        String userId,
        @NotBlank
        @Size(min = 8, message = "Password must be at least 8 characters")
        String newPassword,
        @NotBlank
        String confirmPassword
) {
}