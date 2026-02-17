package com.ekvitou.employee_management.model.service;

import com.ekvitou.employee_management.model.dto.CreateDepartmentDto;
import com.ekvitou.employee_management.model.dto.DepartmentResponseDto;
import com.ekvitou.employee_management.model.dto.UpdateDepartmentDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface DepartmentService {
    DepartmentResponseDto createDepartment(CreateDepartmentDto createDepartmentDto);
    String deleteDepartmentByUuid(String uuid);
    DepartmentResponseDto updateDepartmentByUuid(String uuid, UpdateDepartmentDto updateDepartmentDto);
    DepartmentResponseDto getDepartmentByUuid(String uuid);
    Page<DepartmentResponseDto> getAllDepartments(Pageable pageable);
}
