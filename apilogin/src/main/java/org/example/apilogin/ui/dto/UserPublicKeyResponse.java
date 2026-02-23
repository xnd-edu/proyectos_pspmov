package org.example.apilogin.ui.dto;

import java.time.LocalDateTime;

public record UserPublicKeyResponse(
        Long id,
        Long userId,
        String publicKeyBase64,
        String serverSignatureBase64,
        LocalDateTime createdAt
) {
}
