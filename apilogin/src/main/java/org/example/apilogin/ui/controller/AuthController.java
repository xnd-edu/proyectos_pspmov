package org.example.apilogin.ui.controller;

import jakarta.servlet.http.HttpSession;
import org.example.apilogin.common.Constantes;
import org.example.apilogin.domain.model.Rol;
import org.example.apilogin.domain.model.Usuario;
import org.example.apilogin.domain.service.UsuarioService;
import org.example.apilogin.ui.dto.LoginDTO;
import org.example.apilogin.ui.dto.LoginResponse;
import org.example.apilogin.ui.dto.RegisterRequest;
import org.example.apilogin.ui.dto.UsuarioDTO;
import org.example.apilogin.ui.service.AuthService;
import org.example.apilogin.ui.service.EmailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
public class AuthController {
    private final AuthService authService;
    private final UsuarioService usuarioService;
    private final EmailService emailService;

    public AuthController(AuthService authService, UsuarioService usuarioService, EmailService emailService) {
        this.authService = authService;
        this.usuarioService = usuarioService;
        this.emailService = emailService;
    }

    @PostMapping(Constantes.API_LOGIN)
    public ResponseEntity<LoginResponse> login(@RequestBody LoginDTO request, HttpSession session) {
        Usuario usuario = authService.login(request.username(), request.password(), session);

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

    @PostMapping(Constantes.API_LOGOUT)
    public ResponseEntity<String> logout(HttpSession session) {
        authService.logout(session);
        return ResponseEntity.ok(Constantes.MSG_LOGOUT_EXITOSO);
    }

    @PostMapping(Constantes.API_REGISTER)
    public ResponseEntity<LoginResponse> register(@RequestBody RegisterRequest request) {
        if (usuarioService.existsByUsername(request.username())) {
            LoginResponse response = new LoginResponse("El username ya existe");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        String hashedPassword = usuarioService.encodePassword(request.password());
        String codigoActivacion = UUID.randomUUID().toString();

        Usuario newUser = new Usuario(
                null,
                request.username(),
                hashedPassword,
                request.email(),
                request.nombre(),
                false,
                codigoActivacion,
                LocalDateTime.now().plusHours(24),
                Rol.USER
        );

        newUser = usuarioService.register(newUser);

        emailService.enviarEmailActivacion(newUser.username(), newUser.email(), newUser.codigoActivacion());

        UsuarioDTO usuarioDTO = new UsuarioDTO(
                newUser.id(),
                newUser.username(),
                newUser.email(),
                newUser.nombre(),
                newUser.rol()
        );

        LoginResponse response = new LoginResponse(usuarioDTO, "Te has registrado exitosamente. Por favor, revisa tu correo para activar tu cuenta.");
        return ResponseEntity.ok(response);
    }
}
