package org.example.apilogin.data.entities;

import jakarta.persistence.*;
import org.example.apilogin.common.Constantes;

import java.time.LocalDateTime;

@Entity
@Table(name = "vault_secrets")
public class SecretEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Lob
    @Column(name = "encrypted_data", nullable = false)
    private byte[] encryptedData;

    @Column(name = "iv", nullable = false, length = Constantes.IV_SIZE)
    private byte[] iv;

    @Column(name = "salt", nullable = false, length = Constantes.SALT_SIZE)
    private byte[] salt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
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

    public byte[] getEncryptedData() {
        return encryptedData;
    }

    public void setEncryptedData(byte[] encryptedData) {
        this.encryptedData = encryptedData;
    }

    public byte[] getIv() {
        return iv;
    }

    public void setIv(byte[] iv) {
        this.iv = iv;
    }

    public byte[] getSalt() {
        return salt;
    }

    public void setSalt(byte[] salt) {
        this.salt = salt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

