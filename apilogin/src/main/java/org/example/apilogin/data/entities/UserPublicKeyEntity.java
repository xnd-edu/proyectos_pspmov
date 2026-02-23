package org.example.apilogin.data.entities;

import jakarta.persistence.*;
import org.example.apilogin.common.Constantes;

import java.time.LocalDateTime;

/**
 * Almacena las claves públicas de los usuarios para compartir secretos
 *
 * Cada usuario tiene un par de claves RSA/EC:
 * - Clave pública: Guardada aquí (para que otros cifren)
 * - Clave privada: Solo el usuario la tiene (en su dispositivo/keystore)
 */
@Entity
@Table(name = Constantes.USER_PUBLIC_KEYS_TABLE)
public class UserPublicKeyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = Constantes.USER_PUBLIC_KEY_USER_ID_COLUMN, nullable = false, unique = true)
    private Long userId;

    @Lob
    @Column(name = Constantes.USER_PUBLIC_KEY_COLUMN, nullable = false)
    private byte[] publicKey; // Clave pública codificada (X.509 format)

    @Lob
    @Column(name = Constantes.USER_PUBLIC_KEY_SERVER_SIGNATURE_COLUMN)
    private byte[] serverSignature; // Firma del servidor sobre la clave pública

    @Column(name = Constantes.USER_PUBLIC_KEY_CREATED_AT_COLUMN, nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        // Solo asigna si no fue fijado previamente (el service lo fija antes de firmar
        // para que el mensaje firmado y el createdAt persistido sean idénticos)
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    // Getters y Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public byte[] getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(byte[] publicKey) {
        this.publicKey = publicKey;
    }

    public byte[] getServerSignature() {
        return serverSignature;
    }

    public void setServerSignature(byte[] serverSignature) {
        this.serverSignature = serverSignature;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
