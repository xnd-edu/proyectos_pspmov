package org.example.apilogin.ui.controller;

import org.example.apilogin.common.Constantes;
import org.example.apilogin.domain.model.Usuario;
import org.example.apilogin.domain.service.UsuarioService;
import org.example.apilogin.ui.dto.*;
import org.example.apilogin.ui.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
public class TwoFactorAuthController {
    private final AuthService authService;
    private final UsuarioService usuarioService;

    public TwoFactorAuthController(AuthService authService, UsuarioService usuarioService) {
        this.authService = authService;
        this.usuarioService = usuarioService;
    }

    @PostMapping(Constantes.API_ENABLE_2FA)
    public ResponseEntity<Enable2FAResponse> enable2FA(
            @RequestParam String method,
            Authentication authentication
    ) {
        Long userId = Long.parseLong(authentication.getName());
        Usuario usuario = usuarioService.findById(userId);

        return authService.enable2FA(method, usuario);
    }

    @PostMapping(Constantes.API_CONFIRM_2FA)
    public ResponseEntity<Confirm2FAResponse> confirm2FA(
            @RequestBody Confirm2FADTO request,
            Authentication authentication
    ) {
        Long userId = Long.parseLong(authentication.getName());
        Usuario usuario = usuarioService.findById(userId);

        return authService.confirm2FA(request.code(), usuario);
    }

    @PostMapping(Constantes.API_VERIFY_2FA)
    public ResponseEntity<LoginResponse> verify2FA(
            @RequestBody Verify2FADTO request,
            @RequestHeader(Constantes.HEADER_X_PRETOKEN) String preToken
    ) {

        return authService.verify2FA(request.code(), preToken);
    }
}
