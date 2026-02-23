package org.example.apilogin.ui.dto;

public record LoginResponse(
        boolean success,
        String message,
        UsuarioDTO usuario,
        TokenResponse tokens
) {
    // Constructor con tokens (para JWT)
    public LoginResponse(UsuarioDTO usuario, TokenResponse tokens, String message) {
        this(true, message, usuario, tokens);
    }

    // Constructor solo mensaje de error
    public LoginResponse(String message) {
        this(false, message, null, null);
    }
}

