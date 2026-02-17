package com.ekvitou.employee_management.mapper;

import com.ekvitou.employee_management.model.dto.PositionResponseDto;
import com.ekvitou.employee_management.model.entity.Position;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PositionMapstruct {
    @Mapping(source = "positionName", target = "name")
    @Mapping(source = "positionLevel", target = "level")
    PositionResponseDto mapFromPositionToPositionResponseDto(Position position);
}