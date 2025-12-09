package org.example.apilogin.domain.service;

import org.example.apilogin.common.Constantes;
import org.example.apilogin.data.UsuarioRepository;
import org.example.apilogin.data.entities.UsuarioEntity;
import org.example.apilogin.domain.errores.ForbiddenException;
import org.example.apilogin.domain.errores.ResourceNotFoundException;
import org.example.apilogin.domain.mapper.UsuarioMapper;
import org.example.apilogin.domain.model.Usuario;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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
                .orElseThrow(() -> new ResourceNotFoundException(Constantes.MSG_USUARIO_NO_ENCONTRADO));
        if (!entity.isActivado()) {
            throw new ForbiddenException(Constantes.MSG_CUENTA_NO_ACTIVADA);
        }
        if (passwordEncoder.matches(password, entity.getPassword())) {
            return usuarioMapper.toDomain(entity);
        }
        throw new ForbiddenException(Constantes.MSG_CREDENCIALES_INVALIDAS);
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
                .orElseThrow(() -> new ResourceNotFoundException(Constantes.MSG_CODIGO_ACTIVACION_NO_ENCONTRADO));
        if (entity.getFechaExpiracionCodigo().isBefore(LocalDateTime.now())) {
            throw new ForbiddenException(Constantes.MSG_CODIGO_ACTIVACION_EXPIRADO);
        }
        if (entity.isActivado()) {
            throw new ForbiddenException(Constantes.MSG_CUENTA_YA_ACTIVADA);
        }

        entity.setActivado(true);
        return usuarioMapper.toDomain(usuarioRepository.save(entity));
    }

}
