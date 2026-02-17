package com.ekvitou.employee_management.exception;

public class KeycloakOperationException extends RuntimeException{
    public KeycloakOperationException(String message){
        super(message);
    }
}
