package org.example.apilogin.data.entities;

import jakarta.persistence.*;
import org.example.apilogin.common.Constantes;

import java.time.LocalDateTime;

@Entity
@Table(name = Constantes.VAULT_SECRETS_TABLE)
public class SecretEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = Constantes.SECRET_USER_ID_COLUMN, nullable = false)
    private Long userId;

    @Lob
    @Column(name = Constantes.SECRET_ENCRYPTED_DATA_COLUMN, nullable = false)
    private byte[] encryptedData;

    @Column(name = Constantes.SECRET_IV_COLUMN, nullable = false, length = Constantes.IV_SIZE)
    private byte[] iv;

    @Column(name = Constantes.SECRET_SALT_COLUMN, nullable = false, length = Constantes.SALT_SIZE)
    private byte[] salt;

    @Column(name = Constantes.SECRET_CREATED_AT_COLUMN, nullable = false, updatable = false)
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
