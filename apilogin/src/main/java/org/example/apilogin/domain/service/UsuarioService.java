package org.example.apilogin.domain.service;

import org.example.apilogin.data.UsuarioRepository;
import org.example.apilogin.data.entities.UsuarioEntity;
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
        UsuarioEntity entity = usuarioRepository.findByUsername(username);
        if (entity != null && passwordEncoder.matches(password, entity.getPassword())) {
            return usuarioMapper.toDomain(entity);
        }
        return null;
    }

}
