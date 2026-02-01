package org.example.apilogin.ui.service;

import com.google.zxing.WriterException;
import org.example.apilogin.common.Constantes;
import org.example.apilogin.domain.model.Rol;
import org.example.apilogin.domain.model.TwoFactorMethod;
import org.example.apilogin.domain.model.Usuario;
import org.example.apilogin.domain.service.TokenService;
import org.example.apilogin.domain.service.UsuarioService;
import org.example.apilogin.ui.dto.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

@Service
public class AuthService {
    private final UsuarioService usuarioService;
    private final EmailService emailService;
    private final TotpService totpService;
    private final JwtService jwtService;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final Random random = new Random();


    public AuthService(UsuarioService usuarioService,
                      EmailService emailService,
                      TotpService totpService,
                      JwtService jwtService,
                      TokenService tokenService,
                      AuthenticationManager authenticationManager,
                      UserDetailsService userDetailsService) {
        this.usuarioService = usuarioService;
        this.emailService = emailService;
        this.totpService = totpService;
        this.jwtService = jwtService;
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
    }

    public ResponseEntity<LoginResponse> login(String username, String password) {
        // Autenticar con Spring Security
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        // Cargar usuario completo para obtener datos y verificar 2FA
        Usuario usuario = usuarioService.findByUsername(username);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (Boolean.TRUE.equals(usuario.twoFactorEnabled())) {
            // Generar pre-token para 2FA
            String preToken = jwtService.generatePreToken(userDetails);

            LoginResponse response = new LoginResponse(Constantes.MSG_2FA_REQUIRED);
            // Devolver el pre-token en el header (el controller lo pondrá en el header de respuesta)
            return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .header(Constantes.HEADER_X_PRETOKEN, preToken)
                    .body(response);
        }

        // Login exitoso sin 2FA - generar tokens reales
        TokenResponse tokens = jwtService.generateTokens(userDetails);

        // Guardar tokens en BD
        tokenService.saveTokens(tokens.accessToken(), tokens.refreshToken(), usuario.id());

        UsuarioDTO usuarioDTO = new UsuarioDTO(
                usuario.id(),
                usuario.username(),
                usuario.email(),
                usuario.nombre(),
                usuario.rol()
        );

        LoginResponse response = new LoginResponse(usuarioDTO, tokens, Constantes.MSG_LOGIN_EXITOSO);
        return ResponseEntity.ok(response);
    }

    public Usuario register(String username, String password, String email, String nombre) {
        String hashedPassword = usuarioService.encodePassword(password);
        String codigoActivacion = UUID.randomUUID().toString();

        Usuario newUser = new Usuario(
                null,
                username,
                hashedPassword,
                email,
                nombre,
                false,
                codigoActivacion,
                LocalDateTime.now().plusHours(24),
                Rol.USER
        );

        newUser = usuarioService.register(newUser);

        emailService.enviarEmailActivacion(newUser.username(), newUser.email(), newUser.codigoActivacion());

        return newUser;
    }

    public ResponseEntity<Enable2FAResponse> enable2FA(String method, Usuario usuario) {
        if (Boolean.TRUE.equals(usuario.twoFactorEnabled())) {
            Enable2FAResponse response = new Enable2FAResponse(Constantes.MSG_2FA_YA_ACTIVADO);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        if (TwoFactorMethod.TOTP.name().equalsIgnoreCase(method)) {
            try {
                String secret = totpService.generateSecret();
                String qrCodeUrl = totpService.generateQrCodeImageUri(secret, usuario.username(), Constantes.APP_NAME);

                Usuario updatedUsuario = usuario.set2FA(false, TwoFactorMethod.TOTP, secret);
                usuarioService.update2FA(updatedUsuario);

                Enable2FAResponse response = new Enable2FAResponse(secret, qrCodeUrl, Constantes.MSG_2FA_ENABLED);
                return ResponseEntity.ok(response);
            } catch (WriterException | IOException _) {
                Enable2FAResponse response = new Enable2FAResponse(Constantes.MSG_ERROR_QR_CODE);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        } else if (TwoFactorMethod.EMAIL.name().equalsIgnoreCase(method)) {
            send2FACodeByEmail(usuario);
            Enable2FAResponse response = new Enable2FAResponse(Constantes.MSG_2FA_EMAIL_SENT);
            return ResponseEntity.ok(response);
        } else {
            Enable2FAResponse response = new Enable2FAResponse(Constantes.MSG_METODO_2FA_NO_SOPORTADO);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    public ResponseEntity<Confirm2FAResponse> confirm2FA(String code, Usuario usuario) {
        if (usuario.twoFactorMethod() == null) {
            Confirm2FAResponse response = new Confirm2FAResponse(false, Constantes.MSG_NO_PROCESO_2FA);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }

        boolean isValid = false;

        if (usuario.twoFactorMethod() == TwoFactorMethod.TOTP) {
            isValid = totpService.verifyCode(usuario.twoFactorSecret(), code);
        } else if (usuario.twoFactorMethod() == TwoFactorMethod.EMAIL) {
            if (usuario.fechaExpiracionCodigo() == null ||
                    usuario.fechaExpiracionCodigo().isBefore(LocalDateTime.now())) {
                Confirm2FAResponse response = new Confirm2FAResponse(false, Constantes.MSG_CODIGO_2FA_EXPIRADO);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            }
            isValid = usuario.twoFactorSecret().equals(code);
        }

        if (isValid) {
            Usuario updatedUsuario = usuario.set2FA(true, usuario.twoFactorMethod(), usuario.twoFactorSecret());
            usuarioService.update2FA(updatedUsuario);

            Confirm2FAResponse response = new Confirm2FAResponse(true, Constantes.MSG_2FA_CONFIRMED);
            return ResponseEntity.ok(response);
        }

        Confirm2FAResponse response = new Confirm2FAResponse(false, Constantes.MSG_CREDENCIALES_INVALIDAS);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    public ResponseEntity<LoginResponse> verify2FA(String code, String preToken) {
        // Validar que el pre-token tenga el claim PENDING2FA
        Boolean isPending2FA = jwtService.extractClaim(preToken,
            claims -> claims.get(Constantes.PENDING2FA, Boolean.class));

        if (!Boolean.TRUE.equals(isPending2FA)) {
            LoginResponse response = new LoginResponse(Constantes.MSG_NO_PROCESO_2FA);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }

        Long userId = jwtService.extractUserId(preToken);

        if (!jwtService.isTokenValid(preToken)) {
            LoginResponse response = new LoginResponse(Constantes.MSG_CODIGO_2FA_EXPIRADO);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        Usuario usuario = usuarioService.findById(userId);

        if (Boolean.FALSE.equals(usuario.twoFactorEnabled())) {
            LoginResponse response = new LoginResponse(Constantes.MSG_NO_PROCESO_2FA);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }

        boolean isValid = false;

        if (usuario.twoFactorMethod() == TwoFactorMethod.TOTP) {
            isValid = totpService.verifyCode(usuario.twoFactorSecret(), code);
        } else if (usuario.twoFactorMethod() == TwoFactorMethod.EMAIL) {
            if (usuario.fechaExpiracionCodigo() == null ||
                usuario.fechaExpiracionCodigo().isBefore(LocalDateTime.now())) {
                LoginResponse response = new LoginResponse(Constantes.MSG_CODIGO_2FA_EXPIRADO);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            }
            isValid = usuario.twoFactorSecret().equals(code);
        }

        if (isValid) {
            TokenResponse tokens = jwtService.generateTokens(usuario.rol().name(), usuario.id());

            // Guardar tokens en BD
            tokenService.saveTokens(tokens.accessToken(), tokens.refreshToken(), usuario.id());

            UsuarioDTO usuarioDTO = new UsuarioDTO(
                    usuario.id(),
                    usuario.username(),
                    usuario.email(),
                    usuario.nombre(),
                    usuario.rol()
            );

            LoginResponse response = new LoginResponse(usuarioDTO, tokens, Constantes.MSG_LOGIN_EXITOSO);
            return ResponseEntity.ok(response);
        }

        LoginResponse response = new LoginResponse(Constantes.MSG_CREDENCIALES_INVALIDAS);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    private void send2FACodeByEmail(Usuario usuario) {
        String code = String.valueOf(random.nextInt(100000,999999));

        emailService.enviarCodigo2FA(usuario.username(), usuario.email(), code);

        Usuario updatedUsuario = new Usuario(
                usuario.id(),
                usuario.username(),
                usuario.password(),
                usuario.email(),
                usuario.nombre(),
                usuario.activado(),
                usuario.codigoActivacion(),
                LocalDateTime.now().plusMinutes(10),
                usuario.rol(),
                usuario.twoFactorEnabled(),
                TwoFactorMethod.EMAIL,
                code
        );

        usuarioService.update2FA(updatedUsuario);
    }
}

