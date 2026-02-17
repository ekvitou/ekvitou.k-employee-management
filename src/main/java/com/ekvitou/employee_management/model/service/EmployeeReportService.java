package com.ekvitou.employee_management.model.service;

import com.ekvitou.employee_management.model.entity.EmployeeStatus;

public interface EmployeeReportService {

    byte[] exportEmployeesToCsv(
            String departmentUuid,
            String positionUuid,
            EmployeeStatus status
    );

    byte[] exportEmployeesToExcel(
            String departmentUuid,
            String positionUuid,
            EmployeeStatus status
    );
}