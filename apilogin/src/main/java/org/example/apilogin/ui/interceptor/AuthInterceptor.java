package org.example.apilogin.ui.interceptor;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.apilogin.common.Constantes;
import org.example.apilogin.domain.errores.ForbiddenException;
import org.example.apilogin.domain.errores.UnauthorizedException;
import org.example.apilogin.domain.model.Rol;
import org.example.apilogin.domain.service.TokenService;
import org.example.apilogin.ui.service.JwtService;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    private final JwtService jwtService;
    private final TokenService tokenService;

    public AuthInterceptor(JwtService jwtService, TokenService tokenService) {
        this.jwtService = jwtService;
        this.tokenService = tokenService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ForbiddenException, UnauthorizedException {
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }

        String path = request.getRequestURI();

        // Rutas públicas que no requieren autenticación
        if (path.startsWith(Constantes.API_LOGIN) ||
            path.startsWith(Constantes.API_REGISTER) ||
            path.startsWith(Constantes.API_ACTIVAR_CUENTA) ||
            path.equals(Constantes.API_VERIFY_2FA)) {
            return true;
        }

        RequiresAuth requiresAuth = handlerMethod.getMethodAnnotation(RequiresAuth.class);
        if (requiresAuth != null) {
            String authHeader = request.getHeader(Constantes.JWT_HEADER_AUTHORIZATION);

            if (authHeader == null ||
                !authHeader.startsWith(Constantes.JWT_TOKEN_TYPE_BEARER) ||
                authHeader.length() <= Constantes.JWT_BEARER_PREFIX_LENGTH) {
                throw new UnauthorizedException(Constantes.MSG_DEBE_INICIAR_SESION);
            }

            String token = authHeader.substring(Constantes.JWT_BEARER_PREFIX_LENGTH);

            try {
                // Verificar si el token está revocado en BD
                if (tokenService.isTokenRevoked(token)) {
                    throw new UnauthorizedException(Constantes.MSG_DEBE_INICIAR_SESION);
                }

                String rol = jwtService.extractRol(token);
                Long userId = jwtService.extractUserId(token);

                if (!jwtService.isTokenValid(token)) {
                    throw new UnauthorizedException(Constantes.MSG_DEBE_INICIAR_SESION);
                }

                if (requiresAuth.rol() != Rol.USER && !requiresAuth.rol().name().equals(rol)) {
                    throw new ForbiddenException(Constantes.MSG_ACCESO_DENEGADO);
                }

                request.setAttribute(Constantes.ATTR_ROL, rol);
                request.setAttribute(Constantes.ATTR_USER_ID, userId);
            } catch (MalformedJwtException |
                     SignatureException |
                     ExpiredJwtException |
                     IllegalArgumentException e) {
                throw new UnauthorizedException(Constantes.MSG_DEBE_INICIAR_SESION);
            }
        }

        return true;
    }
}
