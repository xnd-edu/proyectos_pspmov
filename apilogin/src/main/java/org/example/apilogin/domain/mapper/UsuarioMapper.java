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
                e.isActivado(),
                e.getCodigoActivacion(),
                e.getFechaExpiracionCodigo(),
                e.getRol()
        );
    }

    public UsuarioEntity toEntity(Usuario u) {
        if (u == null) return null;
        UsuarioEntity entity = new UsuarioEntity();
        entity.setId(u.id());
        entity.setUsername(u.username());
        entity.setPassword(u.password());
        entity.setEmail(u.email());
        entity.setNombre(u.nombre());
        entity.setActivado(u.activado());
        entity.setCodigoActivacion(u.codigoActivacion());
        entity.setFechaExpiracionCodigo(u.fechaExpiracionCodigo());
        entity.setRol(u.rol());
        return entity;
    }
}
