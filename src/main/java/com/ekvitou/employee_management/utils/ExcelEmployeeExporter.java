package com.ekvitou.employee_management.utils;

import com.ekvitou.employee_management.exception.EmployeeExportException;
import com.ekvitou.employee_management.model.entity.Employee;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Set;

public class ExcelEmployeeExporter {
    private ExcelEmployeeExporter() {}

    public static byte[] exportEmployees(Set<Employee> employees) {

        if (employees == null || employees.isEmpty()) {
            throw new EmployeeExportException("No employees found to export");
        }

        try (Workbook workbook = new XSSFWorkbook()) {

            Sheet sheet = workbook.createSheet("Employees");

            String[] columns = {
                    "UUID", "First Name", "Last Name",
                    "Email", "Department", "Position", "Status"
            };

            Row header = sheet.createRow(0);
            for (int i = 0; i < columns.length; i++) {
                header.createCell(i).setCellValue(columns[i]);
            }

            int rowIdx = 1;

            for (Employee e : employees) {

                Row row = sheet.createRow(rowIdx++);

                String departmentName = e.getDepartment() != null
                        ? e.getDepartment().getDepartmentName()
                        : "";

                String positionName = e.getPosition() != null
                        ? e.getPosition().getPositionName()
                        : "";

                String status = e.getStatus() != null
                        ? e.getStatus().name()
                        : "";

                row.createCell(0).setCellValue(e.getUuid());
                row.createCell(1).setCellValue(e.getFirstName());
                row.createCell(2).setCellValue(e.getLastName());
                row.createCell(3).setCellValue(e.getEmail());
                row.createCell(4).setCellValue(departmentName);
                row.createCell(5).setCellValue(positionName);
                row.createCell(6).setCellValue(status);
            }

            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return out.toByteArray();

        } catch (IOException ex) {
            throw new EmployeeExportException("Failed to export employees to Excel", ex);
        }
    }
}
