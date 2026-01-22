package org.example.apilogin.domain.service;

import jakarta.transaction.Transactional;
import org.example.apilogin.data.TokenRepository;
import org.example.apilogin.data.entities.TokenEntity;
import org.example.apilogin.domain.model.TokenType;
import org.example.apilogin.ui.service.JwtService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class TokenService {

    private final TokenRepository tokenRepository;
    private final JwtService jwtService;
    private final Logger logger = Logger.getLogger(TokenService.class.getName());

    public TokenService(TokenRepository tokenRepository, JwtService jwtService) {
        this.tokenRepository = tokenRepository;
        this.jwtService = jwtService;
    }

    public void saveToken(String token, Long userId, String username, TokenType tokenType) {
        try {
            TokenEntity tokenEntity = new TokenEntity();
            tokenEntity.setToken(token);
            tokenEntity.setUserId(userId);
            tokenEntity.setUsername(username);
            tokenEntity.setTokenType(tokenType);
            tokenEntity.setRevoked(false);
            tokenEntity.setCreatedAt(LocalDateTime.now());

            Date expirationDate = jwtService.extractExpiration(token);
            tokenEntity.setExpiresAt(convertToLocalDateTime(expirationDate));

            tokenRepository.save(tokenEntity);
        } catch (Exception e) {
            // Si falla guardar el token (tabla no existe, etc), solo lo logueamos
            // El sistema sigue funcionando sin persistencia de tokens
            logger.log(Level.WARNING, "Token save failed", e);
        }
    }

    public boolean isTokenRevoked(String token) {
        try {
            return tokenRepository.findByToken(token)
                    .map(TokenEntity::isRevoked)
                    .orElse(false); // Si no está en BD, no está revocado (tokens antiguos)
        } catch (Exception e) {
            // Si falla la consulta (tabla no existe, etc), asumimos que no está revocado
            logger.log(Level.WARNING, "Token isRevoked failed", e);
            return false;
        }
    }

    @Transactional
    public void revokeToken(String token) {
        try {
            tokenRepository.revokeToken(token);
        } catch (Exception e) {
            logger.log(Level.WARNING, "Token revoke failed", e);
        }
    }

    private LocalDateTime convertToLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), java.time.ZoneId.systemDefault());
    }
}

