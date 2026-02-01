package org.example.apilogin.ui.controller;

import org.example.apilogin.common.Constantes;
import org.example.apilogin.domain.model.Usuario;
import org.example.apilogin.domain.service.TokenService;
import org.example.apilogin.domain.service.UsuarioService;
import org.example.apilogin.ui.dto.LoginDTO;
import org.example.apilogin.ui.dto.LoginResponse;
import org.example.apilogin.ui.dto.RegisterDTO;
import org.example.apilogin.ui.dto.UsuarioDTO;
import org.example.apilogin.ui.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    private final AuthService authService;
    private final UsuarioService usuarioService;
    private final TokenService tokenService;

    public AuthController(AuthService authService,
                          UsuarioService usuarioService,
                          TokenService tokenService) {
        this.authService = authService;
        this.usuarioService = usuarioService;
        this.tokenService = tokenService;
    }

    @PostMapping(Constantes.API_LOGIN)
    public ResponseEntity<LoginResponse> login(@RequestBody LoginDTO request) {
        return authService.login(request.username(), request.password());
    }

    @PostMapping(Constantes.API_LOGOUT)
    public ResponseEntity<String> logout(@RequestHeader(value = Constantes.JWT_HEADER_AUTHORIZATION, required = false) String authHeader) {
        if (authHeader != null && authHeader.startsWith(Constantes.JWT_TOKEN_TYPE_BEARER)) {
            String token = authHeader.substring(Constantes.JWT_BEARER_PREFIX_LENGTH);
            tokenService.revokeToken(token);
        }
        return ResponseEntity.ok(Constantes.MSG_LOGOUT_EXITOSO);
    }

    @PostMapping(Constantes.API_REGISTER)
    public ResponseEntity<LoginResponse> register(@RequestBody RegisterDTO request) {
        if (usuarioService.existsByUsername(request.username())) {
            LoginResponse response = new LoginResponse(Constantes.MSG_USERNAME_YA_EXISTE);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        Usuario newUser = authService.register(
                request.username(),
                request.password(),
                request.email(),
                request.nombre()
        );

        UsuarioDTO usuarioDTO = new UsuarioDTO(
                newUser.id(),
                newUser.username(),
                newUser.email(),
                newUser.nombre(),
                newUser.rol()
        );

        LoginResponse response = new LoginResponse(usuarioDTO, Constantes.MSG_REGISTRO_EXITOSO);
        return ResponseEntity.ok(response);
    }
}

