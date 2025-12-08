package org.example.apilogin.ui.dto;

import org.springframework.http.HttpStatus;

public record ErrorResponse(
        String error,
        int status,
        long timestamp
) {
    public ErrorResponse(String message, HttpStatus status) {
        this(message, status.value(), System.currentTimeMillis());
    }
}

