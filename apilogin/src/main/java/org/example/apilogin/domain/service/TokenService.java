package org.example.apilogin.domain.service;

import jakarta.transaction.Transactional;
import org.example.apilogin.common.Constantes;
import org.example.apilogin.data.TokenRepository;
import org.example.apilogin.data.entities.TokenEntity;
import org.example.apilogin.domain.errores.ResourceNotFoundException;
import org.example.apilogin.domain.model.TokenType;
import org.example.apilogin.ui.service.JwtService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;

@Service
public class TokenService {

    private final TokenRepository tokenRepository;
    private final JwtService jwtService;

    public TokenService(TokenRepository tokenRepository, JwtService jwtService) {
        this.tokenRepository = tokenRepository;
        this.jwtService = jwtService;
    }

    public void saveToken(String token, Long userId, TokenType tokenType) {
        TokenEntity tokenEntity = new TokenEntity();
        tokenEntity.setToken(token);
        tokenEntity.setUserId(userId);
        tokenEntity.setTokenType(tokenType);
        tokenEntity.setRevoked(false);
        tokenEntity.setCreatedAt(LocalDateTime.now());

        Date expirationDate = jwtService.extractExpiration(token);
        tokenEntity.setExpiresAt(convertToLocalDateTime(expirationDate));

        tokenRepository.save(tokenEntity);
    }

    @Transactional
    public void saveTokens(String accessToken, String refreshToken, Long userId) {
        saveToken(accessToken, userId, TokenType.ACCESS);
        saveToken(refreshToken, userId, TokenType.REFRESH);
    }

    public boolean isTokenRevoked(String token) {
        return tokenRepository.findByToken(token)
                .map(TokenEntity::isRevoked)
                .orElse(false);
    }

    @Transactional
    public void revokeToken(String token) {
        TokenEntity tokenEntity = tokenRepository.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException(Constantes.MSG_TOKEN_NO_ENCONTRADO));

        if (tokenEntity.isRevoked()) {
            return;
        }

        tokenRepository.revokeToken(token);
    }

    @Transactional
    public void revokeAllUserTokens(Long userId) {
        tokenRepository.findByUserId(userId).forEach(tokenEntity -> {
            if (!tokenEntity.isRevoked()) {
                tokenRepository.revokeToken(tokenEntity.getToken());
            }
        });
    }

    private LocalDateTime convertToLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), java.time.ZoneId.systemDefault());
    }
}

