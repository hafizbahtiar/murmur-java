package com.hafizbahtiar.murmur.common.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Map;

@Builder
public class ApiErrorResponse {
    private String error;
    private String message;
    private Integer status;

    @JsonFormat(pattern = "yyyy-mm-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;

    private String path;
    private Map<String, String> validationErrors;

    public static ApiErrorResponse of(String error, String message, Integer status, String path) {
        return ApiErrorResponse.builder()
                .error(error)
                .message(message)
                .status(status)
                .timestamp(LocalDateTime.now())
                .path(path)
                .build();
    }

    public static ApiErrorResponse of(String error, String message, Integer status, String path, Map<String, String> validationErrors) {
        return ApiErrorResponse.builder()
                .error(error)
                .message(message)
                .status(status)
                .timestamp(LocalDateTime.now())
                .path(path)
                .validationErrors(validationErrors)
                .build();
    }
}
