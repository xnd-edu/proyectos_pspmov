package org.example.apilogin.ui.controller;

import jakarta.servlet.http.HttpSession;
import org.example.apilogin.common.Constantes;
import org.example.apilogin.domain.model.Usuario;
import org.example.apilogin.ui.dto.LoginDTO;
import org.example.apilogin.ui.dto.LoginResponse;
import org.example.apilogin.ui.dto.UsuarioDTO;
import org.example.apilogin.ui.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(Constantes.API_LOGIN)
    public ResponseEntity<LoginResponse> login(@RequestBody LoginDTO request, HttpSession session) {
        Usuario usuario = authService.login(request.username(), request.password(), session);

        if (usuario != null) {
            UsuarioDTO usuarioDTO = new UsuarioDTO(
                    usuario.id(),
                    usuario.username(),
                    usuario.email(),
                    usuario.nombre(),
                    usuario.rol()
            );

            LoginResponse response = new LoginResponse(usuarioDTO, Constantes.MSG_LOGIN_EXITOSO);
            return ResponseEntity.ok(response);
        }

        LoginResponse errorResponse = new LoginResponse(Constantes.MSG_CREDENCIALES_INVALIDAS);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @PostMapping(Constantes.API_LOGOUT)
    public ResponseEntity<String> logout(HttpSession session) {
        authService.logout(session);
        return ResponseEntity.ok(Constantes.MSG_LOGOUT_EXITOSO);
    }
}
