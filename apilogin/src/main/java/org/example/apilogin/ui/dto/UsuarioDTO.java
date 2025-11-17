package org.example.apilogin.ui.dto;

import org.example.apilogin.domain.model.Rol;

public record UsuarioDTO(
        Long id,
        String username,
        String email,
        String nombre,
        Rol rol
) {}

