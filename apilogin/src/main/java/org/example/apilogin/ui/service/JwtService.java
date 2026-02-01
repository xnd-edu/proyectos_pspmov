package org.example.apilogin.ui.service;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.example.apilogin.common.Constantes;
import org.example.apilogin.domain.errores.UnauthorizedException;
import org.example.apilogin.ui.dto.TokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value(Constantes.SPRING_JWT_SECRET)
    private String secretKey;
    @Value(Constantes.SPRING_JWT_EXPIRATION)
    private long jwtExpiration;
    @Value(Constantes.SPRING_JWT_REFRESH_EXPIRATION)
    private long refreshExpiration;
    @Value(Constantes.SPRING_2FA_CODE_EXPIRATION)
    private long twoFactorCodeExpiration;

    public String extractRol(String token) {
        return extractClaim(token, claims -> (String)claims.get(Constantes.JWT_CLAIM_AUTH));
    }

    public Long extractUserId(String token) {
        return extractClaim(token, claims -> {
            Object userId = claims.get(Constantes.JWT_CLAIM_USER_ID);
            if (userId == null) {
                return null;
            }
            // JWT puede deserializar números como Double, Integer o Long
            if (userId instanceof Number number) {
                return number.longValue();
            }
            throw new IllegalStateException(String.format(Constantes.JWT_USER_ID_INVALID_TYPE, userId.getClass()));
        });
    }


    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(String rol, Long userId) {
        Map<String, Object> claims = buildStandardClaims(rol, userId);
        return buildToken(claims, userId.toString(), jwtExpiration);
    }

    public String generateRefreshToken(String rol, Long userId) {
        Map<String, Object> claims = buildStandardClaims(rol, userId);
        return buildToken(claims, userId.toString(), refreshExpiration);
    }

    public String generatePreToken(String rol, Long userId) {
        Map<String, Object> claims = build2FAPendingClaims(rol, userId);
        return buildToken(claims, userId.toString(), twoFactorCodeExpiration);
    }

    public TokenResponse generateTokens(String rol, Long userId) {
        String accessToken = generateToken(rol, userId);
        String refreshToken = generateRefreshToken(rol, userId);
        return new TokenResponse(accessToken, refreshToken);
    }

    public TokenResponse refreshTokens(String refreshToken) {
        String rol = extractRol(refreshToken);
        Long userId = extractUserId(refreshToken);

        if (isTokenExpired(refreshToken)) {
            throw new UnauthorizedException(Constantes.JWT_REFRESH_TOKEN_INVALIDO);
        }

        return generateTokens(rol, userId);
    }

    private Map<String, Object> buildStandardClaims(String rol, Long userId) {
        return Map.of(
                Constantes.JWT_CLAIM_AUTH, rol,
                Constantes.JWT_CLAIM_USER_ID, userId
        );
    }

    private Map<String, Object> build2FAPendingClaims(String rol, Long userId) {
        return Map.of(
                Constantes.JWT_CLAIM_AUTH, rol,
                Constantes.JWT_CLAIM_USER_ID, userId,
                Constantes.PENDING2FA, true
        );
    }


    private String buildToken(
            Map<String, Object> extraClaims,
            String subject,
            long expiration
    ) {


        return Jwts
                .builder()
                .claims(extraClaims)
                .subject(subject)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey())
                .compact();
    }

    public boolean isTokenValid(String token) {
        if (isTokenExpired(token)) {
            return false;
        }

        // Verificar que el subject coincida con el userId en los claims
        String subject = extractClaim(token, Claims::getSubject);
        Long userId = extractUserId(token);

        if (subject == null || userId == null) {
            return false;
        }

        // El subject debe ser igual al userId convertido a String
        return subject.equals(userId.toString());
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith((SecretKey) getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private Key getSignInKey() {
        try {
            final MessageDigest digest = MessageDigest.getInstance(Constantes.JWT_ALGORITHM_SHA512);
            digest.update(secretKey.getBytes(StandardCharsets.UTF_8));
            final SecretKeySpec key2 = new SecretKeySpec(
                    digest.digest(), 0, Constantes.JWT_KEY_SIZE, Constantes.JWT_ALGORITHM_AES);
            return Keys.hmacShaKeyFor(key2.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(Constantes.JWT_LOG_ERROR_FIRMA, e);
        }
    }
}
