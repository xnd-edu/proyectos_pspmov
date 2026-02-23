package org.example.apilogin.ui.dto;

public record RegisterResponse(
        boolean success,
        String message,
        UsuarioDTO usuario,
        TokenResponse tokens,
        String serverSignatureBase64
) {
    public RegisterResponse(UsuarioDTO usuario, String message, TokenResponse tokens, String serverSignatureBase64) {
        this(true, message, usuario, tokens, serverSignatureBase64);
    }

    // Constructor solo mensaje de error
    public RegisterResponse(String message) {
        this(false, message, null, null, null);
    }
}

