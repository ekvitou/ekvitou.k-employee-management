package com.ekvitou.employee_management.authService;

import com.ekvitou.employee_management.exception.*;
import com.ekvitou.employee_management.mapper.UserMapstruct;
import com.ekvitou.employee_management.model.dto.CreateUserDto;
import com.ekvitou.employee_management.model.dto.UserResponseDto;
import com.ekvitou.employee_management.model.entity.Employee;
import com.ekvitou.employee_management.model.entity.User;
import com.ekvitou.employee_management.model.repository.EmployeeRepository;
import com.ekvitou.employee_management.model.repository.UserRepository;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpStatus;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl {
    private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);
    private final Keycloak keycloak;
    private final UserRepository userRepository;
    private final UserMapstruct userMapstruct;
    private final EmployeeRepository employeeRepository;

    @Value("${keycloak.main-realm}")
    private String realm;
    // Extract Keycloak ID from response
    private String getUserIdFromKeycloak(Response response) {
        if (response.getLocation() == null) {
            throw new KeycloakOperationException("No location returned by Keycloak");
        }
        return response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");
    }
    // Send email verification or password update
    private void sendEmailVerification(Keycloak keycloak, UserRepresentation user, String userId, String realm, List<String> actions) {
        user.setRequiredActions(actions);
        keycloak.realm(realm).users().get(userId).update(user);
        keycloak.realm(realm).users().get(userId).executeActionsEmail(actions);
    }

    // Register a new user in Keycloak and local DB
    @Transactional
    public UserResponseDto registerUser(CreateUserDto createUserDto) {
        String username = createUserDto.username().trim();
        String role = createUserDto.role().trim().toUpperCase();
        String email = createUserDto.email().trim().toLowerCase();

        Employee employee = employeeRepository.findEmployeeByUuid(createUserDto.employeeUuid())
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found"));

        // Check for duplicate username or email in DB
        if (userRepository.existsByUsername(username)) {
            throw new DuplicateUserException("Username already exists");
        }
        if (userRepository.existsByEmail(email)) {
            throw new DuplicateUserException("Email already exists");
        }
        if (!keycloak.realm(realm).users().searchByEmail(email, true).isEmpty()) {
            throw new DuplicateUserException("Email already exists in Keycloak");
        }
        // Prepare Keycloak user
        UserRepresentation keycloakUser = new UserRepresentation();
        keycloakUser.setUsername(username);
        keycloakUser.setEmail(email);
        keycloakUser.setFirstName(username);
        keycloakUser.setLastName(username);
        keycloakUser.setEnabled(true);
        // Set temporary password
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setTemporary(true);
        credential.setValue(UUID.randomUUID().toString());
        credential.setType(CredentialRepresentation.PASSWORD);
        keycloakUser.setCredentials(List.of(credential));
        Response responseForException;
        try (Response response = keycloak.realm(realm).users().create(keycloakUser)) {
            responseForException = response;
            if (response.getStatus() == HttpStatus.SC_CREATED) {
                String keycloakId = getUserIdFromKeycloak(response);
                // Save in local DB
                User user = new User();
                user.setKeycloakId(keycloakId);
                user.setUsername(username);
                user.setEmail(email); // save email in local DB
                user.setRole(User.Role.valueOf(role));
                user.setIsDeleted(false);
                user.setEmployee(employee);
                userRepository.save(user);
                // Send verification email to let user set password
                sendEmailVerification(keycloak, keycloakUser, keycloakId, realm, List.of("VERIFY_EMAIL"));
                return userMapstruct.mapFromUserToUserResponseDto(user);
            }
        } catch (Exception e) {
            log.error("Keycloak user creation failed", e);
            throw new KeycloakOperationException("Keycloak user creation failed: " + e.getMessage());
        }
        throw new KeycloakOperationException("Failed to create user. Status code: " + responseForException.getStatus());
    }

    // Reset user password
    public String resetPassword(ResetPasswordDto resetPasswordDto) {
        if (!resetPasswordDto.newPassword().equals(resetPasswordDto.confirmPassword())) {
            throw new InvalidRequestException("Passwords do not match");
        }
        UserRepresentation user = keycloak.realm(realm).users().get(resetPasswordDto.userId()).toRepresentation();
        if (user == null) {
            throw new UserNotFoundException("User not found");
        }
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(resetPasswordDto.newPassword());
        credential.setTemporary(false);
        keycloak.realm(realm).users().get(resetPasswordDto.userId()).resetPassword(credential);
        return user.getId();
    }

    // Forgot password flow
    public String forgotPassword(String email) {
        UserRepresentation user = keycloak.realm(realm).users().searchByEmail(email, true).stream().findFirst()
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
        sendEmailVerification(keycloak, user, user.getId(), realm, List.of("UPDATE_PASSWORD"));
        return user.getId();
    }
}
