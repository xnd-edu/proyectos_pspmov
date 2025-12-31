package org.example.apilogin.domain.model;

import java.io.Serializable;
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
        Rol rol,
        Boolean twoFactorEnabled,
        TwoFactorMethod twoFactorMethod,
        String twoFactorSecret
) implements Serializable {
    public Usuario(Long id, String username, String password, String email, String nombre, boolean activado,
                   String codigoActivacion, LocalDateTime fechaExpiracionCodigo, Rol rol) {
        this(id, username, password, email, nombre, activado, codigoActivacion, fechaExpiracionCodigo, rol, false, TwoFactorMethod.NONE, "");
    }

    public Usuario set2FA(Boolean enabled, TwoFactorMethod method, String secret) {
        return new Usuario(this.id, this.username, this.password, this.email, this.nombre, this.activado,
                this.codigoActivacion, this.fechaExpiracionCodigo, this.rol, enabled, method, secret);
    }
}
