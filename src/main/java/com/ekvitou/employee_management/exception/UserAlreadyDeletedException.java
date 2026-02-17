package com.ekvitou.employee_management.exception;

public class UserAlreadyDeletedException extends RuntimeException{
    public UserAlreadyDeletedException(String message){
        super(message);
    }
}
