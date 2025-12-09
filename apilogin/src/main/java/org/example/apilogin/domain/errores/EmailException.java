package org.example.apilogin.domain.errores;

public class EmailException extends RuntimeException {
    public EmailException(String message) {
        super(message);
    }
}

