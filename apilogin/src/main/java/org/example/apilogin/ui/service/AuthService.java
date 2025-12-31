package org.example.apilogin.ui.service;

import com.google.zxing.WriterException;
import jakarta.servlet.http.HttpSession;
import org.example.apilogin.common.Constantes;
import org.example.apilogin.domain.model.Rol;
import org.example.apilogin.domain.model.TwoFactorMethod;
import org.example.apilogin.domain.model.Usuario;
import org.example.apilogin.domain.service.UsuarioService;
import org.example.apilogin.ui.dto.Confirm2FAResponse;
import org.example.apilogin.ui.dto.Enable2FAResponse;
import org.example.apilogin.ui.dto.LoginResponse;
import org.example.apilogin.ui.dto.UsuarioDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private final Random random = new Random();


    public AuthService(UsuarioService usuarioService, EmailService emailService, TotpService totpService) {
        this.usuarioService = usuarioService;
        this.emailService = emailService;
        this.totpService = totpService;
    }

    public Usuario login(String username, String password, HttpSession session) {
        Usuario usuario = usuarioService.login(username, password);

        if (Boolean.FALSE.equals(usuario.twoFactorEnabled())) {
            session.setAttribute(Constantes.SESSION_ATTR_USUARIO, usuario);
        } else {
            if (usuario.twoFactorMethod() == TwoFactorMethod.EMAIL) {
                send2FACodeByEmail(usuario);
            }
            // Solo guardar el ID del usuario, guardar el codigo/secreto en la sesión rompería la base del 2FA
            session.setAttribute(Constantes.SESSION_ATTR_2FA_PENDING_USER_ID, usuario.id());
        }
        return usuario;
    }

    public void logout(HttpSession session) {
        session.invalidate();
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

    public boolean isAuthenticated(HttpSession session) {
        return session.getAttribute(Constantes.SESSION_ATTR_USUARIO) != null;
    }

    public Long getUsuarioIdFromSession(HttpSession session) {
        return ((Usuario) session.getAttribute(Constantes.SESSION_ATTR_USUARIO)).id();
    }

    private Long getPendingUsuarioIdFromSession(HttpSession session) {
        return (Long) session.getAttribute(Constantes.SESSION_ATTR_2FA_PENDING_USER_ID);
    }

    public Rol getRolFromSession(HttpSession session) {
        return session.getAttribute(Constantes.SESSION_ATTR_USUARIO) != null
            ? ((Usuario) session.getAttribute(Constantes.SESSION_ATTR_USUARIO)).rol()
            : null;
    }

    public boolean isAdmin(HttpSession session) {
        Rol rol = getRolFromSession(session);
        return Rol.ADMIN.equals(rol);
    }

    public boolean isUser(HttpSession session) {
        return !isAdmin(session);
    }

    public ResponseEntity<Enable2FAResponse> enable2FA(String method, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute(Constantes.SESSION_ATTR_USUARIO);

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

    public ResponseEntity<Confirm2FAResponse> confirm2FA(String code, HttpSession session) {
        Usuario usuario = usuarioService.findById(getUsuarioIdFromSession(session));

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

    public ResponseEntity<LoginResponse> verify2FA(String code, HttpSession session) {
        Long pendingUserId = getPendingUsuarioIdFromSession(session);

        if (pendingUserId == null) {
            LoginResponse response = new LoginResponse(Constantes.MSG_NO_PROCESO_2FA);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }

        Usuario pendingUser = usuarioService.findById(pendingUserId);

        if (Boolean.FALSE.equals(pendingUser.twoFactorEnabled())) {
            LoginResponse response = new LoginResponse(Constantes.MSG_NO_PROCESO_2FA);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }

        boolean isValid = false;

        if (pendingUser.twoFactorMethod() == TwoFactorMethod.TOTP) {
            isValid = totpService.verifyCode(pendingUser.twoFactorSecret(), code);
        } else if (pendingUser.twoFactorMethod() == TwoFactorMethod.EMAIL) {
            if (pendingUser.fechaExpiracionCodigo() == null ||
                pendingUser.fechaExpiracionCodigo().isBefore(LocalDateTime.now())) {
                LoginResponse response = new LoginResponse(Constantes.MSG_CODIGO_2FA_EXPIRADO);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            }
            isValid = pendingUser.twoFactorSecret().equals(code);
        }

        if (isValid) {
            session.removeAttribute(Constantes.SESSION_ATTR_2FA_PENDING_USER_ID);
            session.setAttribute(Constantes.SESSION_ATTR_USUARIO, pendingUser);

            UsuarioDTO usuarioDTO = new UsuarioDTO(
                    pendingUser.id(),
                    pendingUser.username(),
                    pendingUser.email(),
                    pendingUser.nombre(),
                    pendingUser.rol()
            );

            LoginResponse response = new LoginResponse(usuarioDTO, Constantes.MSG_LOGIN_EXITOSO);
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
