package com.ekvitou.employee_management.utils;

import com.ekvitou.employee_management.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.Date;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private ResponseEntity<APIErrorResponse> buildErrorResponse(String message, HttpStatus status) {

        APIErrorResponse apiErrorResponse = APIErrorResponse
                .builder()
                .status(status.toString())
                .errorMessage(message)
                .timeStamp(Date.from(Instant.now()))
                .build();

        return ResponseEntity.status(status).body(apiErrorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<APIErrorResponse> handleValidationError(MethodArgumentNotValidException exception) {
        String message = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getDefaultMessage())
                .findFirst()
                .orElse("Validation failed");

        return buildErrorResponse(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({
            EmployeeNotFoundException.class,
            UserNotFoundException.class,
            DepartmentNotFoundException.class,
            PositionNotFoundException.class
    })
    public ResponseEntity<APIErrorResponse> handleNotFound(RuntimeException exception) {
        return buildErrorResponse(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({
            EmployeeAlreadyDeletedException.class,
            UserAlreadyDeletedException.class,
            DepartmentAlreadyDeletedException.class,
            PositionAlreadyDeletedException.class
    })
    public ResponseEntity<APIErrorResponse> handleAlreadyDeleted(RuntimeException exception) {
        return buildErrorResponse(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({
            DuplicateEmployeeException.class,
            DuplicateUserException.class,
            DuplicateDepartmentException.class,
            DuplicatePositionException.class
    })
    public ResponseEntity<APIErrorResponse> handleDuplicate(RuntimeException exception) {
        return buildErrorResponse(exception.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler({
            DepartmentDeleteException.class,
            PositionDeleteException.class,
            InvalidRequestException.class,
            InvalidEmployeeStatusException.class
    })
    public ResponseEntity<APIErrorResponse> handleDeleteException(RuntimeException exception) {
        return buildErrorResponse(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(KeycloakOperationException.class)
    public ResponseEntity<APIErrorResponse> handleKeycloakError(KeycloakOperationException exception) {
        log.error("Keycloak error: {}", exception.getMessage(), exception);
        return buildErrorResponse(exception.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<APIErrorResponse> handleDatabaseError(DataIntegrityViolationException exception) {
        log.error("Database integrity violation: {}", exception.getMessage(), exception);
        return buildErrorResponse("Database integrity violation", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<APIErrorResponse> handleUnhandledException(Exception exception) {
        log.error("Unhandled exception occurred: {}", exception.getMessage(), exception);

        return buildErrorResponse("Unexpected server error", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
