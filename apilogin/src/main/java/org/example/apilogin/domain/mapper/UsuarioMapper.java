package org.example.apilogin.domain.mapper;

import org.example.apilogin.data.entities.UsuarioEntity;
import org.example.apilogin.domain.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {


    public Usuario toDomain(UsuarioEntity e) {
        if (e == null) return null;
        return new Usuario(
                e.getId(),
                e.getUsername(),
                e.getPassword(),
                e.getEmail(),
                e.getNombre(),
                e.getRol()
        );
    }

    public UsuarioEntity toEntity(Usuario u) {
        if (u == null) return null;
        return new UsuarioEntity(
                u.id(),
                u.username(),
                u.password(),
                u.email(),
                u.nombre(),
                u.rol()
        );
    }
}
