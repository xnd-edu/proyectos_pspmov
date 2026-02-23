package org.example.apilogin.ui.dto;

public record SimShareSecretDTO(
    Long secretId,
    String ownerPassword,
    Long recipientUserId,
    String serverPublicKeyBase64
) {
}

