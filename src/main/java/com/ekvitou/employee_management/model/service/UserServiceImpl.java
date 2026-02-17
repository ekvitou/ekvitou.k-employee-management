package com.ekvitou.employee_management.model.service;

import com.ekvitou.employee_management.exception.*;
import com.ekvitou.employee_management.mapper.UserMapstruct;
import com.ekvitou.employee_management.model.dto.CreateUserDto;
import com.ekvitou.employee_management.model.dto.UpdateUserDto;
import com.ekvitou.employee_management.model.dto.UserResponseDto;
import com.ekvitou.employee_management.model.entity.Employee;
import com.ekvitou.employee_management.model.entity.User;
import com.ekvitou.employee_management.model.repository.EmployeeRepository;
import com.ekvitou.employee_management.model.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final UserMapstruct userMapstruct;

    @Transactional
    @Override
    public UserResponseDto createUser(CreateUserDto dto) {
        String username = dto.username().trim();
        String email = dto.email().trim().toLowerCase();
        if (userRepository.existsByUsername(username)) {
            throw new DuplicateUserException("Username already exists");
        }
        if (userRepository.existsByEmail(email)) {
            throw new DuplicateUserException("Email already exists");
        }
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setRole(User.Role.valueOf(dto.role().trim().toUpperCase()));
        user.setIsDeleted(false);
        if (dto.employeeUuid() != null && !dto.employeeUuid().isBlank()) {
            Employee employee = employeeRepository.findEmployeeByUuid(dto.employeeUuid())
                    .orElseThrow(() -> new EmployeeNotFoundException("Employee not found"));
            if (userRepository.existsByEmployee(employee)) {
                throw new DuplicateEmployeeException("This employee is already linked to a user");
            }
            user.setEmployee(employee);
        }
        userRepository.save(user);
        return userMapstruct.mapFromUserToUserResponseDto(user);
    }

    @Transactional
    @Override
    public UserResponseDto updateUserByUuid(String uuid, UpdateUserDto dto) {
        String username = dto.username().trim();
        String email = dto.email().trim();

        User user = userRepository.findUserByUuid(uuid)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        if (Boolean.TRUE.equals(user.getIsDeleted())) {
            throw new UserAlreadyDeletedException("Cannot update deleted user");
        }
        if (!user.getUsername().equals(username) && userRepository.existsByUsername(username)) {
            throw new DuplicateUserException("Username already exists");
        }
        if (!user.getEmail().equals(email) && userRepository.existsByEmail(email)) {
            throw new DuplicateUserException("Email already exists");
        }
        user.setUsername(username);
        user.setEmail(email);
        user.setRole(User.Role.valueOf(dto.role().trim().toUpperCase()));
        userRepository.save(user);
        return userMapstruct.mapFromUserToUserResponseDto(user);
    }

    @Transactional
    @Override
    public String deleteUserByUuid(String uuid) {
        User user = userRepository.findUserByUuid(uuid)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        if (Boolean.TRUE.equals(user.getIsDeleted())) {
            throw new UserAlreadyDeletedException("User is already deleted");
        }
        user.setIsDeleted(true);
        userRepository.save(user);
        return user.getUuid();
    }

    @Override
    public UserResponseDto getUserByUuid(String uuid) {
        User user = userRepository.findUserByUuid(uuid)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        if (Boolean.TRUE.equals(user.getIsDeleted())) {
            throw new UserAlreadyDeletedException("User is already deleted");
        }
        return userMapstruct.mapFromUserToUserResponseDto(user);
    }

    @Override
    public Page<UserResponseDto> getAllUsers(Pageable pageable) {
        return userRepository.findUsersByIsDeletedIsFalse(pageable)
                .map(userMapstruct::mapFromUserToUserResponseDto);
    }

    @Override
    public UserResponseDto getUserByKeycloakId(String keycloakId) {
        User user = userRepository.findUserByKeycloakId(keycloakId)
                .orElseThrow(() -> new UserNotFoundException("User not found with Keycloak ID: " + keycloakId));
        if (Boolean.TRUE.equals(user.getIsDeleted())) {
            throw new UserAlreadyDeletedException("User is already deleted");
        }
        return userMapstruct.mapFromUserToUserResponseDto(user);
    }

    @Override
    public Page<UserResponseDto> searchUsersByKeyword(String keyword, Pageable pageable) {
        if (keyword == null || keyword.isBlank()) {
            throw new UserNotFoundException("Keyword must not be empty");
        }
        Page<User> users = userRepository.findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCaseAndIsDeletedIsFalse(keyword, keyword, pageable);
        if (users.isEmpty()) {
            throw new UserNotFoundException("No users found for keyword: " + keyword);
        }
        return users.map(userMapstruct::mapFromUserToUserResponseDto);
    }
}
