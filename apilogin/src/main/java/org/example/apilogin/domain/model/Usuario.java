package org.example.apilogin.domain.model;

import java.time.LocalDateTime;

public record Usuario(
        Long id,
        String username,
        String password,
        String email,
        String nombre,
        boolean activado,
        String codigoActivacion,
        LocalDateTime fechaExpiracionCodigo,
        Rol rol
) {}
