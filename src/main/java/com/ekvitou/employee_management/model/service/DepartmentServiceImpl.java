package com.ekvitou.employee_management.model.service;

import com.ekvitou.employee_management.exception.DepartmentAlreadyDeletedException;
import com.ekvitou.employee_management.exception.DepartmentDeleteException;
import com.ekvitou.employee_management.exception.DepartmentNotFoundException;
import com.ekvitou.employee_management.exception.DuplicateDepartmentException;
import com.ekvitou.employee_management.mapper.DepartmentMapstruct;
import com.ekvitou.employee_management.model.dto.CreateDepartmentDto;
import com.ekvitou.employee_management.model.dto.DepartmentResponseDto;
import com.ekvitou.employee_management.model.dto.UpdateDepartmentDto;
import com.ekvitou.employee_management.model.entity.Department;
import com.ekvitou.employee_management.model.repository.DepartmentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final DepartmentMapstruct departmentMapstruct;

    @Transactional
    @Override
    public DepartmentResponseDto createDepartment(CreateDepartmentDto dto) {
        if (departmentRepository.existsByDepartmentNameAndIsDeletedFalse(dto.name())) {
            throw new DuplicateDepartmentException("Department name already exists");
        }
        Department department = new Department();
        department.setDepartmentName(dto.name());
        department.setDepartmentDescription(dto.description());
        department.setIsDeleted(false);
        departmentRepository.save(department);
        return departmentMapstruct.mapFromDepartmentToDepartmentResponseDto(department);
    }

    @Transactional
    @Override
    public String deleteDepartmentByUuid(String uuid) {
        Department department = departmentRepository.findDepartmentByUuid(uuid)
                .orElseThrow(() -> new DepartmentNotFoundException("Department not found"));
        if (department.getIsDeleted()) {
            throw new DepartmentAlreadyDeletedException("Department is already deleted");
        }
        if (!department.getEmployees().isEmpty()) {
            throw new DepartmentDeleteException("Cannot delete department with assigned employees");
        }
        department.setIsDeleted(true);
        departmentRepository.save(department);
        return department.getUuid();
    }

    @Transactional
    @Override
    public DepartmentResponseDto updateDepartmentByUuid(String uuid, UpdateDepartmentDto dto) {
        Department department = departmentRepository.findDepartmentByUuid(uuid)
                .orElseThrow(() -> new DepartmentNotFoundException("Department not found"));
        if (department.getIsDeleted()) {
            throw new DepartmentAlreadyDeletedException("Department is already deleted");
        }
        if (!department.getDepartmentName().equals(dto.name())
                && departmentRepository.existsByDepartmentNameAndIsDeletedFalse(dto.name())) {
            throw new DuplicateDepartmentException("Department name already exists");
        }
        department.setDepartmentName(dto.name());
        department.setDepartmentDescription(dto.description());
        departmentRepository.save(department);
        return departmentMapstruct.mapFromDepartmentToDepartmentResponseDto(department);
    }

    @Override
    public DepartmentResponseDto getDepartmentByUuid(String uuid) {
        Department department = departmentRepository.findDepartmentByUuid(uuid)
                .orElseThrow(() -> new DepartmentNotFoundException("Department not found"));
        if (department.getIsDeleted()) {
            throw new DepartmentAlreadyDeletedException("Department is already deleted");
        }
        return departmentMapstruct.mapFromDepartmentToDepartmentResponseDto(department);
    }

    @Override
    public Page<DepartmentResponseDto> getAllDepartments(Pageable pageable) {
        return departmentRepository
                .findDepartmentsByIsDeletedIsFalse(pageable)
                .map(departmentMapstruct::mapFromDepartmentToDepartmentResponseDto);
    }
}
