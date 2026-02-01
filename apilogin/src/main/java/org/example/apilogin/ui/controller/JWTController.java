package org.example.apilogin.ui.controller;


import org.example.apilogin.common.Constantes;
import org.example.apilogin.domain.errores.UnauthorizedException;
import org.example.apilogin.domain.service.TokenService;
import org.example.apilogin.ui.dto.RefreshTokenRequest;
import org.example.apilogin.ui.dto.TokenResponse;
import org.example.apilogin.ui.service.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constantes.API_JWT)
public class JWTController {

    private final JwtService jwtService;
    private final TokenService tokenService;

    public JWTController(JwtService jwtService, TokenService tokenService) {
        this.jwtService = jwtService;
        this.tokenService = tokenService;
    }

    @PostMapping(Constantes.API_JWT_REFRESH)
    public ResponseEntity<TokenResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        try {
            // Verificar que el refresh token no esté revocado
            if (tokenService.isTokenRevoked(request.refreshToken())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            TokenResponse tokens = jwtService.refreshTokens(request.refreshToken());

            // Revocar el refresh token anterior
            tokenService.revokeToken(request.refreshToken());

            // Guardar los nuevos tokens
            Long userId = jwtService.extractUserId(tokens.accessToken());
            tokenService.saveTokens(tokens.accessToken(), tokens.refreshToken(), userId);

            return ResponseEntity.ok(tokens);
        } catch (UnauthorizedException _) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}

