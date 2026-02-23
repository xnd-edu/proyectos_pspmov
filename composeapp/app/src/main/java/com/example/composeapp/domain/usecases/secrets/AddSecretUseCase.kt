package com.example.composeapp.domain.usecases.secrets

import com.example.composeapp.data.SecretsRepository
import com.example.composeapp.data.security.SymmetricCryptoManager
import javax.inject.Inject

class AddSecretUseCase @Inject constructor(
    private val secretRepository: SecretsRepository,
    private val symmetricCryptoManager: SymmetricCryptoManager
) {
    suspend operator fun invoke(secretText: String, password: String): Result<Long> {
        val salt = symmetricCryptoManager.generateSalt()
        val key = symmetricCryptoManager.generateKeyFromPassword(password, salt)
        val encryptionResult = symmetricCryptoManager.encryptGCM(secretText, key)

        return secretRepository.addSecret(encryptionResult.encryptedData, encryptionResult.iv, symmetricCryptoManager.bytesToBase64(salt))
    }
}

