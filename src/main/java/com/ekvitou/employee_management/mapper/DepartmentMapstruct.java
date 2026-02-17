package com.ekvitou.employee_management.mapper;

import com.ekvitou.employee_management.model.dto.DepartmentResponseDto;
import com.ekvitou.employee_management.model.entity.Department;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DepartmentMapstruct {
    @Mapping(source = "departmentName", target = "name")
    @Mapping(source = "departmentDescription", target = "description")
    DepartmentResponseDto mapFromDepartmentToDepartmentResponseDto(Department department);
}
