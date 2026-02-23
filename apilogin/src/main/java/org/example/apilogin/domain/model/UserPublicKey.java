package org.example.apilogin.domain.model;

import java.time.LocalDateTime;

public record UserPublicKey(
        Long id,
        Long userId,
        byte[] publicKey,
        byte[] serverSignature,
        LocalDateTime createdAt
) {
}
