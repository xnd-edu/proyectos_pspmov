package com.example.composeapp.domain.usecases.secrets

import com.example.composeapp.R
import com.example.composeapp.data.SecretsRepository
import com.example.composeapp.data.security.SymmetricCryptoManager
import com.example.composeapp.domain.model.Secret
import com.example.composeapp.domain.model.SecretDecrypted
import com.example.composeapp.ui.common.StringProvider
import javax.inject.Inject

class GetSecretByIdUseCase @Inject constructor(
    private val secretsRepository: SecretsRepository,
    private val symmetricCryptoManager: SymmetricCryptoManager,
    private val stringProvider: StringProvider
) {
    suspend operator fun invoke(id: Long, password: String): Result<SecretDecrypted> {
        val result = secretsRepository.getSecretById(id)

        return result.mapCatching { dto ->
            val secret = Secret(
                id = dto.id,
                userId = dto.userId,
                encryptedData = symmetricCryptoManager.base64ToBytes(dto.encryptedDataBase64),
                iv = symmetricCryptoManager.base64ToBytes(dto.ivBase64),
                salt = symmetricCryptoManager.base64ToBytes(dto.saltBase64),
                createdAt = dto.createdAt
            )

            val id = secret.id ?: throw IllegalArgumentException(stringProvider.getString(R.string.error_datos_faltantes))
            val encrypted = secret.encryptedData ?: throw IllegalArgumentException(stringProvider.getString(R.string.error_datos_faltantes))
            val iv = secret.iv ?: throw IllegalArgumentException(stringProvider.getString(R.string.error_datos_faltantes))
            val salt = secret.salt ?: throw IllegalArgumentException(stringProvider.getString(R.string.error_datos_faltantes))
            val createdAt = secret.createdAt ?: throw IllegalArgumentException(stringProvider.getString(R.string.error_datos_faltantes))
            val key = symmetricCryptoManager.generateKeyFromPassword(password, salt)

            val decryptedData = symmetricCryptoManager.decryptGCM(
                encrypted,
                key,
                iv
            ).getOrThrow()

            SecretDecrypted(
                id = id,
                decryptedData = decryptedData,
                createdAt = createdAt
            )
        }
    }
}