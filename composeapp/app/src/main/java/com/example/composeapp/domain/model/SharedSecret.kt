package com.example.composeapp.domain.model

data class SharedSecretRequest(
    val secretId: Long,
    val sharedWithUserId: Long,
    val encryptedData: String,
    val encryptedKey: String,
    val iv: String
)
