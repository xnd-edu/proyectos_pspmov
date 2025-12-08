package org.example.apilogin.ui.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.apilogin.common.Constantes;
import org.example.apilogin.domain.errores.ForbiddenException;
import org.example.apilogin.domain.errores.UnauthorizedException;
import org.example.apilogin.domain.model.Rol;
import org.example.apilogin.ui.dto.ErrorResponse;
import org.example.apilogin.ui.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    private final AuthService authService;
    private final ObjectMapper objectMapper;

    public AuthInterceptor(AuthService authService, ObjectMapper objectMapper) {
        this.authService = authService;
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }

        String path = request.getRequestURI();

        if (path.startsWith(Constantes.API_LOGIN) ||
            path.startsWith(Constantes.API_REGISTER) ||
            path.startsWith(Constantes.API_ACTIVAR_CUENTA)) {
            return true;
        }

        RequiresAuth requiresAuth = handlerMethod.getMethodAnnotation(RequiresAuth.class);
        if (requiresAuth != null) {
            if (!authService.isAuthenticated(request.getSession())) {
                throw new UnauthorizedException(Constantes.MSG_DEBE_INICIAR_SESION);
            }

            if (requiresAuth.rol() == Rol.ADMIN && !authService.isAdmin(request.getSession())) {
                throw new ForbiddenException(Constantes.MSG_ACCESO_DENEGADO);
            }
        }

        return true;
    }
}
