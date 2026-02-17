package com.ekvitou.employee_management.exception;

public class DepartmentAlreadyDeletedException extends RuntimeException{
    public DepartmentAlreadyDeletedException(String message) {
        super(message);
    }
}
