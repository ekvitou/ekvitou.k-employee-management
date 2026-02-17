package com.ekvitou.employee_management.authService;

import com.ekvitou.employee_management.model.dto.CreateUserDto;
import com.ekvitou.employee_management.utils.ResponseTemplate;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;
import java.util.Date;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    private final AuthServiceImpl authService;

    @PostMapping("/register")
    public ResponseTemplate<Object> registerUser(@RequestBody @Valid CreateUserDto createUserDto) {
        return ResponseTemplate
                .builder()
                .status(HttpStatus.CREATED.toString())
                .date(Date.from(Instant.now()))
                .message("Successfully created a new user")
                .data(authService.registerUser(createUserDto))
                .build();
    }

    @PutMapping("/reset-password")
    public ResponseTemplate<Object> resetPassword(@RequestBody @Valid ResetPasswordDto resetPasswordDto) {
        return ResponseTemplate
                .builder()
                .status(HttpStatus.OK.toString())
                .date(Date.from(Instant.now()))
                .message("User password reset successfully")
                .data(authService.resetPassword(resetPasswordDto))
                .build();
    }

    @PostMapping("/forgot-password")
    public ResponseTemplate<Object> forgotPassword(@RequestParam String email) {
        return ResponseTemplate
                .builder()
                .status(HttpStatus.OK.toString())
                .date(Date.from(Instant.now()))
                .message("Check your email to reset your password")
                .data(authService.forgotPassword(email))
                .build();
    }
}
