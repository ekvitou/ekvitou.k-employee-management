package com.ekvitou.employee_management.exception;

public class EmployeeExportException extends RuntimeException {
    public EmployeeExportException(String message) {
        super(message);
    }

    public EmployeeExportException(String message, Throwable cause) {
        super(message, cause);
    }
}
