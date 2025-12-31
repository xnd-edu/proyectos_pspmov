package org.example.apilogin.ui.controller;

import jakarta.servlet.http.HttpSession;
import org.example.apilogin.common.Constantes;
import org.example.apilogin.ui.dto.*;
import org.example.apilogin.ui.interceptor.RequiresAuth;
import org.example.apilogin.ui.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class TwoFactorAuthController {
    private final AuthService authService;

    public TwoFactorAuthController(AuthService authService) {
        this.authService = authService;
    }

    @RequiresAuth
    @PostMapping(Constantes.API_ENABLE_2FA)
    public ResponseEntity<Enable2FAResponse> enable2FA(@RequestParam String method, HttpSession session) {
        return authService.enable2FA(method, session);
    }

    @RequiresAuth
    @PostMapping(Constantes.API_CONFIRM_2FA)
    public ResponseEntity<Confirm2FAResponse> confirm2FA(@RequestBody Confirm2FADTO request, HttpSession session) {
        return authService.confirm2FA(request.code(), session);
    }

    @PostMapping(Constantes.API_VERIFY_2FA)
    public ResponseEntity<LoginResponse> verify2FA(@RequestBody Verify2FADTO request, HttpSession session) {
        return authService.verify2FA(request.code(), session);
    }
}
