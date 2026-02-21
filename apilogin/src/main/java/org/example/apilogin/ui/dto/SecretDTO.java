package org.example.apilogin.ui.dto;

public record SecretDTO(
        byte[] encryptedData,
        byte[] iv,
        byte[] salt
) {}
