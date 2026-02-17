package com.ekvitou.employee_management.exception;

public class DuplicatePositionException extends RuntimeException{
    public DuplicatePositionException(String message) {
        super(message);
    }
}
