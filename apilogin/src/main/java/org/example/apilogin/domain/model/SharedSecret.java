package org.example.apilogin.domain.model;

import java.time.LocalDateTime;

public record SharedSecret(
        Long id,
        Long secretId,
        Long ownerId,
        Long sharedWithId,
        byte[] encryptedData,
        byte[] encryptedKey,
        byte[] iv,
        LocalDateTime createdAt
) {
}
