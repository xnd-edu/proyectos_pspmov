package com.example.composeapp.domain.model

import java.time.LocalDateTime

data class Secret(
    val id: Long? = null,
    val userId: Long? = null,
    val encryptedData: ByteArray? = null,
    val iv: ByteArray? = null,
    val salt: ByteArray? = null,
    val createdAt: LocalDateTime? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Secret

        if (id != other.id) return false
        if (userId != other.userId) return false
        if (!encryptedData.contentEquals(other.encryptedData)) return false
        if (!iv.contentEquals(other.iv)) return false
        if (!salt.contentEquals(other.salt)) return false
        if (createdAt != other.createdAt) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + (userId?.hashCode() ?: 0)
        result = 31 * result + (encryptedData?.contentHashCode() ?: 0)
        result = 31 * result + (iv?.contentHashCode() ?: 0)
        result = 31 * result + (salt?.contentHashCode() ?: 0)
        result = 31 * result + (createdAt?.hashCode() ?: 0)
        return result
    }
}

data class SecretRequest(
    val encryptedDataBase64: String,
    val ivBase64: String,
    val saltBase64: String
)

data class SecretResponse(
    val id: Long,
    val userId: Long,
    val encryptedDataBase64: String,
    val ivBase64: String,
    val saltBase64: String,
    val createdAt: LocalDateTime
)

data class SecretDecrypted(
    val id: Long,
    val decryptedData: String,
    val createdAt: LocalDateTime
)

data class SecretCreateResponse(
    val id: Long,
    val createdAt: LocalDateTime
)