package com.example.taskmanagement.config;

import com.example.taskmanagement.exception.RoleAlreadyExistsException;
import com.example.taskmanagement.exception.TaskAlreadyHasExecutorException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleEntityNotFoundException(EntityNotFoundException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("status", String.valueOf(HttpStatus.NOT_FOUND.value()));
        errorResponse.put("message", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TaskAlreadyHasExecutorException.class)
    public ResponseEntity<Map<String, String>> handleTaskAlreadyHasExecutorException(TaskAlreadyHasExecutorException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("status", String.valueOf(HttpStatus.CONFLICT.value()));
        errorResponse.put("message", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(RoleAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleRoleAlreadyExistsException(RoleAlreadyExistsException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("status", String.valueOf(HttpStatus.CONFLICT.value()));
        errorResponse.put("message", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleException(Exception ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("status", String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
        errorResponse.put("message", "Internal server error");
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, String>> handleAccessDeniedException(AccessDeniedException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "У пользователя нет прав");
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Map<String, String>> handleAuthenticationException(AuthenticationException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Логин или пароль неверны");
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleAuthenticationCredentialsNotFoundException(AuthenticationCredentialsNotFoundException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Пользователь не авторизован");
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }
}