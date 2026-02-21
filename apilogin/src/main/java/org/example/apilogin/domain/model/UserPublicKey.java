package org.example.apilogin.domain.model;

import java.time.LocalDateTime;

public record UserPublicKey(
        Long id,
        Long userId,
        byte[] publicKey,
        Integer keySize,
        byte[] serverSignature,
        LocalDateTime signedAt,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserPublicKey that = (UserPublicKey) o;
        return java.util.Objects.equals(id, that.id)
                && java.util.Objects.equals(userId, that.userId)
                && java.util.Arrays.equals(publicKey, that.publicKey)
                && java.util.Objects.equals(keySize, that.keySize)
                && java.util.Arrays.equals(serverSignature, that.serverSignature)
                && java.util.Objects.equals(signedAt, that.signedAt)
                && java.util.Objects.equals(createdAt, that.createdAt)
                && java.util.Objects.equals(updatedAt, that.updatedAt);
    }

    @Override
    public int hashCode() {
        int result = java.util.Objects.hash(id, userId, keySize, signedAt, createdAt, updatedAt);
        result = 31 * result + java.util.Arrays.hashCode(publicKey);
        result = 31 * result + java.util.Arrays.hashCode(serverSignature);
        return result;
    }

    @Override
    public String toString() {
        return "UserPublicKey{" +
                "id=" + id +
                ", userId=" + userId +
                ", publicKey=" + java.util.Arrays.toString(publicKey) +
                ", keySize=" + keySize +
                ", serverSignature=" + java.util.Arrays.toString(serverSignature) +
                ", signedAt=" + signedAt +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
