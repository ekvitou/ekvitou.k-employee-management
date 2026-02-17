package com.ekvitou.employee_management.exception;

public class PositionAlreadyDeletedException extends RuntimeException {
    public PositionAlreadyDeletedException(String message) {
        super(message);
    }
}
