package org.example.apilogin.domain.model;

import java.time.LocalDateTime;

public record Secret(
        Long id,
        Long userId,
        byte[] encryptedData,
        byte[] iv,
        byte[] salt,
        LocalDateTime createdAt
) {
}
