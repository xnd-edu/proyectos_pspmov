package org.example.apilogin.data.entities;

import jakarta.persistence.*;
import org.example.apilogin.common.Constantes;
import org.example.apilogin.domain.model.Rol;

import java.time.LocalDateTime;

@Entity
@Table(name = Constantes.TABLE_USUARIOS)
public class UsuarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private String email;
    private String nombre;
    private boolean activado;
    private String codigoActivacion;
    private LocalDateTime fechaExpiracionCodigo;

    @Enumerated(EnumType.STRING)
    private Rol rol;

    public UsuarioEntity() {
        // Constructor requerido por JPA
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isActivado() {
        return activado;
    }

    public void setActivado(boolean activado) {
        this.activado = activado;
    }

    public String getCodigoActivacion() {
        return codigoActivacion;
    }

    public void setCodigoActivacion(String codigoActivacion) {
        this.codigoActivacion = codigoActivacion;
    }

    public LocalDateTime getFechaExpiracionCodigo() {
        return fechaExpiracionCodigo;
    }

    public void setFechaExpiracionCodigo(LocalDateTime fechaExpiracionCodigo) {
        this.fechaExpiracionCodigo = fechaExpiracionCodigo;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }
}
