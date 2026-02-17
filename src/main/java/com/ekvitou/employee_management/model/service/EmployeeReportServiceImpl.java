package com.ekvitou.employee_management.model.service;

import com.ekvitou.employee_management.model.entity.Employee;
import com.ekvitou.employee_management.model.entity.EmployeeStatus;
import com.ekvitou.employee_management.model.repository.EmployeeRepository;
import com.ekvitou.employee_management.utils.CsvEmployeeExporter;
import com.ekvitou.employee_management.utils.ExcelEmployeeExporter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class EmployeeReportServiceImpl implements EmployeeReportService {
    private final EmployeeRepository employeeRepository;

    @Override
    public byte[] exportEmployeesToCsv(String departmentUuid, String positionUuid, EmployeeStatus status) {
        Set<Employee> employees = employeeRepository.findEmployeesForReport(status, departmentUuid, positionUuid);
        return CsvEmployeeExporter.exportEmployees(employees);
    }

    @Override
    public byte[] exportEmployeesToExcel(String departmentUuid, String positionUuid, EmployeeStatus status) {
        Set<Employee> employees = employeeRepository.findEmployeesForReport(status, departmentUuid, positionUuid);
        return ExcelEmployeeExporter.exportEmployees(employees);
    }
}
