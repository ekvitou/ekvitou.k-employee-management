package com.ekvitou.employee_management.mapper;

import com.ekvitou.employee_management.model.dto.EmployeeResponseDto;
import com.ekvitou.employee_management.model.entity.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", uses = {DepartmentMapstruct.class, PositionMapstruct.class})
public interface EmployeeMapstruct {

    @Mapping(source = "department", target = "department_uuid")
    @Mapping(source = "position", target = "position_uuid")
    @Mapping(source = "status", target = "status", qualifiedByName = "statusToString")
    EmployeeResponseDto mapFromEmployeeToEmployeeResponseDto(Employee employee);

    @Named("statusToString")
    default String statusToString(Enum<?> status) {
        return status != null ? status.name() : null;
    }
}
