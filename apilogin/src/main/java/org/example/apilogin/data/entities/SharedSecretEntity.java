package org.example.apilogin.data.entities;

import jakarta.persistence.*;
import org.example.apilogin.common.Constantes;

import java.time.LocalDateTime;

/**
 * Representa un secreto compartido entre usuarios
 *
 * Flujo:
 * 1. Usuario A (owner) cifra secreto con su password (AES)
 * 2. Usuario A comparte con Usuario B:
 *    - A descifra con su password
 *    - A obtiene publicKey de B
 *    - A cifra con publicKey de B (RSA/EC)
 *    - Se guarda en esta tabla
 * 3. Usuario B descifra con su privateKey
 */
@Entity
@Table(name = "shared_secrets")
public class SharedSecretEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "secret_id", nullable = false)
    private Long secretId; // Referencia al secreto original

    @Column(name = "owner_id", nullable = false)
    private Long ownerId; // Usuario que comparte

    @Column(name = "shared_with_id", nullable = false)
    private Long sharedWithId; // Usuario con quien se comparte

    @Column(name = "encrypted_data", nullable = false)
    @Lob
    private byte[] encryptedData; // El String secreto cifrado con AES

    @Column(name = "encrypted_key", nullable = false)
    private byte[] encryptedKey; // La clave AES cifrada con la Pública

    @Column(name = "iv", nullable = false, length = Constantes.IV_SIZE)
    private byte[] iv; // El IV de la AES

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

    public Long getSecretId() {
        return secretId;
    }

    public void setSecretId(Long secretId) {
        this.secretId = secretId;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Long getSharedWithId() {
        return sharedWithId;
    }

    public void setSharedWithId(Long sharedWithId) {
        this.sharedWithId = sharedWithId;
    }

    public byte[] getEncryptedData() {
        return encryptedData;
    }

    public void setEncryptedData(byte[] encryptedData) {
        this.encryptedData = encryptedData;
    }

    public byte[] getEncryptedKey() {
        return encryptedKey;
    }

    public void setEncryptedKey(byte[] encryptedKey) {
        this.encryptedKey = encryptedKey;
    }

    public byte[] getIv() {
        return iv;
    }

    public void setIv(byte[] iv) {
        this.iv = iv;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
