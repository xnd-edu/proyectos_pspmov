package org.example.apilogin.ui.service;

import jakarta.servlet.http.HttpSession;
import org.example.apilogin.common.Constantes;
import org.example.apilogin.domain.model.Rol;
import org.example.apilogin.domain.model.Usuario;
import org.example.apilogin.domain.service.UsuarioService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthService {
    private final UsuarioService usuarioService;
    private final EmailService emailService;

    public AuthService(UsuarioService usuarioService, EmailService emailService) {
        this.usuarioService = usuarioService;
        this.emailService = emailService;
    }

    public Usuario login(String username, String password, HttpSession session) {
        Usuario usuario = usuarioService.login(username, password);
        session.setAttribute(Constantes.SESSION_ATTR_USUARIO, usuario);
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
}
