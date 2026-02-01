package org.example.apilogin.ui.dto;

import org.example.apilogin.common.Constantes;

public record TokenResponse(
        String accessToken,
        String refreshToken,
        String tokenType
) {
    public TokenResponse(String accessToken, String refreshToken) {
        this(accessToken, refreshToken, Constantes.JWT_TOKEN_TYPE_BEARER);
    }
}

