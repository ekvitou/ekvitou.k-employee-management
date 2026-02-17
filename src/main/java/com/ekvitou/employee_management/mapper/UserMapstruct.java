package com.ekvitou.employee_management.mapper;

import com.ekvitou.employee_management.model.dto.UserResponseDto;
import com.ekvitou.employee_management.model.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapstruct {
    UserResponseDto mapFromUserToUserResponseDto(User user);
}
