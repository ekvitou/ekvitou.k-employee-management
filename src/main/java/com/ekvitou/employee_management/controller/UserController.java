package com.ekvitou.employee_management.controller;

import com.ekvitou.employee_management.model.dto.CreateUserDto;
import com.ekvitou.employee_management.model.dto.UpdateUserDto;
import com.ekvitou.employee_management.model.service.UserService;
import com.ekvitou.employee_management.utils.ResponseTemplate;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;
import java.util.Date;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseTemplate<Object> createUser(@RequestBody @Valid CreateUserDto createUserDto) {
        return ResponseTemplate
                .builder()
                .status(HttpStatus.CREATED.toString())
                .message("Successfully created a user!")
                .date(Date.from(Instant.now()))
                .data(userService.createUser(createUserDto))
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{uuid}")
    public ResponseTemplate<Object> deleteUserByUuid(@PathVariable String uuid) {
        return ResponseTemplate
                .builder()
                .status(HttpStatus.OK.toString())
                .message("Successfully deleted a user!")
                .date(Date.from(Instant.now()))
                .data(userService.deleteUserByUuid(uuid))
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{uuid}")
    public ResponseTemplate<Object> updateUserByUuid(@PathVariable String uuid, @RequestBody @Valid UpdateUserDto updateUserDto) {
        return ResponseTemplate
                .builder()
                .status(HttpStatus.OK.toString())
                .message("Successfully updated a user!")
                .date(Date.from(Instant.now()))
                .data(userService.updateUserByUuid(uuid, updateUserDto))
                .build();
    }

    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    @GetMapping("/{uuid}")
    public ResponseTemplate<Object> getUserByUuid(@PathVariable String uuid) {
        return ResponseTemplate
                .builder()
                .status(HttpStatus.OK.toString())
                .message("Successfully retrieved a user!")
                .date(Date.from(Instant.now()))
                .data(userService.getUserByUuid(uuid))
                .build();
    }

    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    @GetMapping("/keycloak/{keycloakId}")
    public ResponseTemplate<Object> getUserByKeycloakId(@PathVariable String keycloakId) {
        return ResponseTemplate
                .builder()
                .status(HttpStatus.OK.toString())
                .message("Successfully retrieved a user by Keycloak ID!")
                .date(Date.from(Instant.now()))
                .data(userService.getUserByKeycloakId(keycloakId))
                .build();
    }

    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    @GetMapping
    public ResponseTemplate<Object> getAllUsers(Pageable pageable) {
        return ResponseTemplate
                .builder()
                .status(HttpStatus.OK.toString())
                .message("Successfully retrieved all users!")
                .date(Date.from(Instant.now()))
                .data(userService.getAllUsers(pageable))
                .build();
    }

    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    @GetMapping("/search")
    public ResponseTemplate<Object> searchUsersByKeyword(@RequestParam String keyword, Pageable pageable) {
        return ResponseTemplate
                .builder()
                .status(HttpStatus.OK.toString())
                .message("Successfully searched users!")
                .date(Date.from(Instant.now()))
                .data(userService.searchUsersByKeyword(keyword, pageable))
                .build();
    }
}
