package com.hafizbahtiar.murmur.common.exceptions;

import com.hafizbahtiar.murmur.common.dto.ApiResponse;
import com.hafizbahtiar.murmur.features.users.exceptions.UserAlreadyExistsException;
import com.hafizbahtiar.murmur.features.users.exceptions.UserNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError fieldError : ex.getBindingResult()
                .getFieldErrors()) {
            errors.putIfAbsent(fieldError.getField(), fieldError.getDefaultMessage());
        }

        ApiResponse<Map<String, String>> body = ApiResponse.<Map<String, String>>builder()
                .success(false)
                .data(errors)
                .message("Validation failed")
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleConstraintViolation(ConstraintViolationException ex) {
        Map<String, String> errors = ex.getConstraintViolations()
                .stream()
                .collect(Collectors.toMap(cv -> {
                    String path = cv.getPropertyPath().toString();
                    int idx = path.lastIndexOf('.');
                    return idx >= 0 ? path.substring(idx + 1) : path;
                }, ConstraintViolation::getMessage, (existing, replacement) -> existing));

        ApiResponse<Map<String, String>> body = ApiResponse.<Map<String, String>>builder()
                .success(false)
                .data(errors)
                .message("Validation failed")
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleCustomValidation(ValidationException ex) {
        Map<String, String> errors = ex.getValidationErrors();

        ApiResponse<Map<String, String>> body = ApiResponse.<Map<String, String>>builder()
                .success(false)
                .data(errors)
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleUserNotFound(UserNotFoundException ex) {
        ApiResponse<Object> body = ApiResponse.<Object>builder()
                .success(false)
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Object>> handleUserAlreadyExists(UserAlreadyExistsException ex) {
        ApiResponse<Object> body = ApiResponse.<Object>builder()
                .success(false)
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleAll(Exception ex) {
        log.error("Unhandled exception", ex);
        ApiResponse<Object> body = ApiResponse.<Object>builder()
                .success(false)
                .message("An unexpected error occurred")
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}
