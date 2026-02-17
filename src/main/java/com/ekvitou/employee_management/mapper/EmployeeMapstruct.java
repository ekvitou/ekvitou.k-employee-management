//package com.ekvitou.employee_management.mapper;
//
//import com.ekvitou.employee_management.model.dto.DepartmentResponseDto;
//import com.ekvitou.employee_management.model.dto.EmployeeResponseDto;
//import com.ekvitou.employee_management.model.dto.PositionResponseDto;
//import com.ekvitou.employee_management.model.entity.Department;
//import com.ekvitou.employee_management.model.entity.Employee;
//import com.ekvitou.employee_management.model.entity.Position;
//import org.mapstruct.Mapper;
//import org.mapstruct.Mapping;
//import org.mapstruct.Named;
//
//@Mapper(componentModel = "spring")
//public interface EmployeeMapstruct {
//
//    @Mapping(source = "department", target = "department_uuid")
//    @Mapping(source = "position", target = "position_uuid")
//    @Mapping(source = "status", target = "status", qualifiedByName = "mapStatusToString")
//    EmployeeResponseDto mapFromEmployeeToEmployeeResponseDto(Employee employee);
//
//    @Named("mapStatusToString")
//    default String mapStatusToString(Enum<?> status) {
//        return status != null ? status.name() : null;
//    }
//
//    default DepartmentResponseDto map(Department department) {
//        if (department == null) {
//            return null;
//        }
//
//        return new DepartmentResponseDto(
//                department.getUuid(),
//                department.getDepartmentName(),
//                department.getDepartmentDescription()
//        );
//    }
//
//    default PositionResponseDto map(Position position) {
//        if (position == null) {
//            return null;
//        }
//
//        return new PositionResponseDto(
//                position.getUuid(),
//                position.getPositionName(),
//                position.getPositionLevel()
//        );
//    }
//}
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
