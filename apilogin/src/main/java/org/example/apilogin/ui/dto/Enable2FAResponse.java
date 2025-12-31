package org.example.apilogin.ui.dto;

public record Enable2FAResponse(
        String secret,
        String qrCodeUrl,
        String message
) {
    public Enable2FAResponse(String message) {
        this(null, null, message);
    }
}
