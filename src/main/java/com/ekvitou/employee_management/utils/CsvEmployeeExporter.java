package com.ekvitou.employee_management.utils;

import com.ekvitou.employee_management.model.entity.Employee;
import java.nio.charset.StandardCharsets;
import java.util.Set;

public class CsvEmployeeExporter {
    private CsvEmployeeExporter() {}

    public static byte[] exportEmployees(Set<Employee> employees) {

        StringBuilder sb = new StringBuilder();
        sb.append("UUID,First Name,Last Name,Email,Department,Position,Status\n");

        if (employees == null || employees.isEmpty()) {
            return sb.toString().getBytes(StandardCharsets.UTF_8);
        }

        for (Employee e : employees) {

            String departmentName = e.getDepartment() != null
                    ? e.getDepartment().getDepartmentName()
                    : "";

            String positionName = e.getPosition() != null
                    ? e.getPosition().getPositionName()
                    : "";

            String status = e.getStatus() != null
                    ? e.getStatus().name()
                    : "";

            sb.append(csv(e.getUuid())).append(",")
                    .append(csv(e.getFirstName())).append(",")
                    .append(csv(e.getLastName())).append(",")
                    .append(csv(e.getEmail())).append(",")
                    .append(csv(departmentName)).append(",")
                    .append(csv(positionName)).append(",")
                    .append(csv(status))
                    .append("\r\n");
        }

        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }

    private static String csv(String value) {
        return value == null ? "" : "\"" + value.replace("\"", "\"\"") + "\"";
    }
}
