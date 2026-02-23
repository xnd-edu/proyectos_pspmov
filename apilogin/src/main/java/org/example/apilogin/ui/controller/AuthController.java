package org.example.apilogin.ui.controller;

import org.example.apilogin.common.Constantes;
import org.example.apilogin.domain.model.UserPublicKey;
import org.example.apilogin.domain.model.Usuario;
import org.example.apilogin.domain.service.TokenService;
import org.example.apilogin.domain.service.UsuarioService;
import org.example.apilogin.ui.dto.*;
import org.example.apilogin.ui.security.jwt.CustomUserDetailsService;
import org.example.apilogin.ui.service.AuthService;
import org.example.apilogin.ui.service.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;

@RestController
public class AuthController {
    private final AuthService authService;
    private final UsuarioService usuarioService;
    private final TokenService tokenService;
    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    public AuthController(AuthService authService,
                          UsuarioService usuarioService,
                          TokenService tokenService,
                          JwtService jwtService,
                          CustomUserDetailsService userDetailsService) {
        this.authService = authService;
        this.usuarioService = usuarioService;
        this.tokenService = tokenService;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
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

    private RegisterResponse registerUserAndKey(RegisterDTO request, boolean requireActivation) {
        Usuario newUser = requireActivation
                ? authService.register(request.username(), request.password(), request.email(), request.nombre())
                : authService.registerWithoutActivation(request.username(), request.password(), request.email(), request.nombre());

        UserPublicKey registeredKey = authService.registerPublicKey(newUser.id(), request.publicKeyBase64());
        String serverSignatureBase64 = registeredKey.serverSignature() != null
                ? Base64.getEncoder().encodeToString(registeredKey.serverSignature())
                : null;

        UserDetails userDetails = userDetailsService.loadUserByUsername(newUser.username());

        // Login exitoso sin 2FA - generar tokens reales
        TokenResponse tokens = jwtService.generateTokens(userDetails);

        // Guardar tokens en BD
        tokenService.saveTokens(tokens.accessToken(), tokens.refreshToken(), newUser.id());

        UsuarioDTO usuarioDTO = new UsuarioDTO(
                newUser.id(),
                newUser.username(),
                newUser.email(),
                newUser.nombre(),
                newUser.rol()
        );

        return new RegisterResponse(usuarioDTO, Constantes.MSG_REGISTRO_SIN_ACTIVACION_EXITOSO, tokens, serverSignatureBase64);
    }

    @PostMapping(Constantes.API_REGISTER)
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterDTO request) {
        if (usuarioService.existsByUsername(request.username())) {
            RegisterResponse response = new RegisterResponse(Constantes.MSG_USERNAME_YA_EXISTE);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return ResponseEntity.ok(registerUserAndKey(request, true));
    }

    // Endpoint para registrar sin activación por mail (solo para pruebas o casos especiales)
    // NOTA PARA EL PROFESOR: Las pruebas se harían muy pesadas si se requiere activar por mail por cada registro,
    // por lo que este endpoint permite registrar usuarios ya activados para facilitar las pruebas.
    @PostMapping(Constantes.API_REGISTER_NO_ACTIVATION)
    public ResponseEntity<RegisterResponse> registerWithoutActivation(@RequestBody RegisterDTO request) {
        if (usuarioService.existsByUsername(request.username())) {
            RegisterResponse response = new RegisterResponse(Constantes.MSG_USERNAME_YA_EXISTE);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return ResponseEntity.ok(registerUserAndKey(request, false));
    }
}
