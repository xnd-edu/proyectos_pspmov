package org.example.apilogin.ui.dto;

public record SecretDTO(
        String encryptedDataBase64,
        String ivBase64,
        String saltBase64
) {}
