package com.ekvitou.employee_management.model.service;

import com.ekvitou.employee_management.model.dto.CreateUserDto;
import com.ekvitou.employee_management.model.dto.UpdateUserDto;
import com.ekvitou.employee_management.model.dto.UserResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    UserResponseDto createUser(CreateUserDto createUserDto);
    String deleteUserByUuid(String uuid);
    UserResponseDto updateUserByUuid(String uuid, UpdateUserDto updateUserDto);
    UserResponseDto getUserByUuid(String uuid);
    Page<UserResponseDto> getAllUsers(Pageable pageable);
    UserResponseDto getUserByKeycloakId(String keycloakId);
    Page<UserResponseDto> searchUsersByKeyword(String keyword, Pageable pageable);
}
