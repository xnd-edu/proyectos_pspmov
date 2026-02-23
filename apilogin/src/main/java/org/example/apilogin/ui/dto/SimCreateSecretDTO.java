package org.example.apilogin.ui.dto;

public record SimCreateSecretDTO(
        String plainText,
        String password
) {}
