package org.example.apilogin.ui.controller;


import org.example.apilogin.common.Constantes;
import org.example.apilogin.domain.errores.UnauthorizedException;
import org.example.apilogin.domain.model.TokenType;
import org.example.apilogin.domain.service.TokenService;
import org.example.apilogin.ui.dto.RefreshTokenRequest;
import org.example.apilogin.ui.dto.TokenResponse;
import org.example.apilogin.ui.service.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@RestController
@RequestMapping(Constantes.API_JWT)
public class JWTController {

    private final JwtService jwtService;
    private final TokenService tokenService;
    private final Logger logger = Logger.getLogger(JWTController.class.getName());

    public JWTController(JwtService jwtService, TokenService tokenService) {
        this.jwtService = jwtService;
        this.tokenService = tokenService;
    }

    @GetMapping(Constantes.API_JWT_VALIDATE)
    public String validateToken(@RequestHeader(Constantes.JWT_HEADER_AUTHORIZATION) String authHeader, @RequestParam String username){
        String token = authHeader.substring(Constantes.JWT_BEARER_PREFIX_LENGTH);

        // Verificar si el token está revocado
        if (tokenService.isTokenRevoked(token)) {
            throw new UnauthorizedException("Token revocado");
        }

        jwtService.isTokenValid(token, username);
        logger.info(jwtService.extractRol(token));
        return jwtService.extractUsername(token);
    }

    /**
     * Endpoint para refrescar tokens JWT
     */
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
            String username = jwtService.extractUsername(tokens.accessToken());
            Long userId = jwtService.extractUserId(tokens.accessToken());

            tokenService.saveToken(tokens.accessToken(), userId, username,
                    TokenType.ACCESS);
            tokenService.saveToken(tokens.refreshToken(), userId, username,
                    TokenType.REFRESH);

            return ResponseEntity.ok(tokens);
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}

