package com.ekvitou.employee_management.exception;

public class EmployeeAlreadyDeletedException extends RuntimeException{
    public EmployeeAlreadyDeletedException(String message) {
        super(message);
    }
}
