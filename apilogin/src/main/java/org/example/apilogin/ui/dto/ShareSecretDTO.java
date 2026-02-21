package org.example.apilogin.ui.dto;

public record ShareSecretDTO(
    Long secretId,           // ID del secreto a compartir
    Long sharedWithUserId,   // ID del usuario con quien compartir
    String encryptedData,    // Secreto cifrado con la clave AES (Base64)
    String encryptedKey,     // Clave AES cifrada con la clave pública del receptor (Base64)
    String iv               // IV utilizado para cifrar el secreto (Base64)
) {
}

