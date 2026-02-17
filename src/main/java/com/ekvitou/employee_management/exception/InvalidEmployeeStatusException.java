package com.ekvitou.employee_management.exception;

public class InvalidEmployeeStatusException extends RuntimeException {
    public InvalidEmployeeStatusException(String message) {
        super(message);
    }
}
