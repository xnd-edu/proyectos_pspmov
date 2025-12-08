package org.example.apilogin.domain.service;

import org.example.apilogin.data.UsuarioRepository;
import org.example.apilogin.data.entities.UsuarioEntity;
import org.example.apilogin.domain.errores.ForbiddenException;
import org.example.apilogin.domain.errores.ResourceNotFoundException;
import org.example.apilogin.domain.mapper.UsuarioMapper;
import org.example.apilogin.domain.model.Usuario;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(
            UsuarioRepository usuarioRepository,
            UsuarioMapper usuarioMapper,
            PasswordEncoder passwordEncoder
    ) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario login(String username, String password) {
        UsuarioEntity entity = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró un usuario con el nombre de usuario proporcionado."));
        if (!entity.isActivado()) {
            throw new ForbiddenException("La cuenta no está activada.");
        }
        if (passwordEncoder.matches(password, entity.getPassword())) {
            return usuarioMapper.toDomain(entity);
        }
        throw new ForbiddenException("Credenciales inválidas.");
    }

    public Usuario register(Usuario usuario) {
        return usuarioMapper.toDomain(usuarioRepository.save(usuarioMapper.toEntity(usuario)));
    }

    public boolean existsByUsername(String username) {
        return usuarioRepository.findByUsername(username).isPresent();
    }

    public String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    public Usuario activarCuenta(String codigoActivacion) {
        UsuarioEntity entity = usuarioRepository.findByCodigoActivacion(codigoActivacion)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se encontró un usuario con el código de activación proporcionado."));

        if (entity.isActivado()) {
            throw new ForbiddenException("La cuenta ya está activada.");
        }

        entity.setActivado(true);
        return usuarioMapper.toDomain(usuarioRepository.save(entity));
    }

}
