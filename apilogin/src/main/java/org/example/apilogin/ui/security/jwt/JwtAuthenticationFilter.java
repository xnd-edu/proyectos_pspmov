package org.example.apilogin.ui.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.apilogin.common.Constantes;
import org.example.apilogin.domain.service.TokenService;
import org.example.apilogin.ui.service.JwtService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtro que intercepta cada petición para validar el token JWT
 * Se ejecuta una vez por cada request (OncePerRequestFilter)
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final TokenService tokenService;

    public JwtAuthenticationFilter(JwtService jwtService, TokenService tokenService) {
        this.jwtService = jwtService;
        this.tokenService = tokenService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader(Constantes.JWT_HEADER_AUTHORIZATION);

        // Si no hay header o no empieza con "Bearer ", continuar con el siguiente filtro
        if (authHeader == null || !authHeader.startsWith(Constantes.JWT_TOKEN_TYPE_BEARER)) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extraer el token (quitar "Bearer ")
        final String jwt = authHeader.substring(Constantes.JWT_BEARER_PREFIX_LENGTH);

        // Si no hay autenticación previa en el contexto
        if (SecurityContextHolder.getContext().getAuthentication() == null) {

            // Verificar si el token está revocado
            if (tokenService.isTokenRevoked(jwt)) {
                filterChain.doFilter(request, response);
                return;
            }

            // Validar el token (verifica firma, expiración y coherencia interna)
            if (jwtService.isTokenValid(jwt)) {

                // Extraer información del token
                String userId = jwtService.extractUserId(jwt).toString();
                String rol = jwtService.extractRol(jwt);

                // Crear UserDetails desde el token (sin llamada a BD)
                UserDetails userDetails = User.builder()
                        .username(userId)
                        .password("")  // No necesitamos el password
                        .roles(rol)
                        .build();

                // Crear el objeto de autenticación
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                // Añadir detalles de la request
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Establecer la autenticación en el contexto de seguridad
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Continuar con el siguiente filtro
        filterChain.doFilter(request, response);
    }
}

