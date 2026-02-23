package com.example.composeapp.domain.model

import java.time.LocalDateTime

data class UserPublicKeyResponse(
    val id: Long,
    val userId: Long,
    val publicKeyBase64: String,
    val serverSignatureBase64: String,
    val createdAt: LocalDateTime
)