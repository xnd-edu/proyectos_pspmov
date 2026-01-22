package org.example.apilogin.ui.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.example.apilogin.common.Constantes;
import org.example.apilogin.domain.model.Usuario;
import org.example.apilogin.domain.service.UsuarioService;
import org.example.apilogin.ui.dto.*;
import org.example.apilogin.ui.interceptor.RequiresAuth;
import org.example.apilogin.ui.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class TwoFactorAuthController {
    private final AuthService authService;
    private final UsuarioService usuarioService;

    public TwoFactorAuthController(AuthService authService, UsuarioService usuarioService) {
        this.authService = authService;
        this.usuarioService = usuarioService;
    }

    @RequiresAuth
    @PostMapping(Constantes.API_ENABLE_2FA)
    public ResponseEntity<Enable2FAResponse> enable2FA(
            @RequestParam String method,
            HttpServletRequest request
    ) {
        String username = (String) request.getAttribute(Constantes.ATTR_USUARIO);
        Usuario usuario = usuarioService.findByUsername(username);

        return authService.enable2FA(method, usuario);
    }

    @RequiresAuth
    @PostMapping(Constantes.API_CONFIRM_2FA)
    public ResponseEntity<Confirm2FAResponse> confirm2FA(
            @RequestBody Confirm2FADTO request,
            HttpServletRequest httpRequest
    ) {
        String username = (String) httpRequest.getAttribute(Constantes.ATTR_USUARIO);
        Usuario usuario = usuarioService.findByUsername(username);

        return authService.confirm2FA(request.code(), usuario);
    }

    @PostMapping(Constantes.API_VERIFY_2FA)
    public ResponseEntity<LoginResponse> verify2FA(
            @RequestBody Verify2FADTO request,
            @RequestHeader(Constantes.JWT_HEADER_AUTHORIZATION) String authHeader
    ) {
        // Extraer el pre-token del header "Authorization: Bearer {preToken}"
        String preToken = authHeader.substring(7);

        return authService.verify2FA(request.code(), preToken);
    }
}
