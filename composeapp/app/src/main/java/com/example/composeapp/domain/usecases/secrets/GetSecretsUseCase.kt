package com.example.composeapp.domain.usecases.secrets

import com.example.composeapp.data.SecretsRepository
import com.example.composeapp.data.security.SymmetricCryptoManager
import com.example.composeapp.domain.model.Secret
import javax.inject.Inject

class GetSecretsUseCase @Inject constructor(
    private val secretsRepository: SecretsRepository,
    private val symmetricCryptoManager: SymmetricCryptoManager
) {
    suspend operator fun invoke(): Result<List<Secret>> {
        val result = secretsRepository.getSecrets()

        return result.map { responseList ->
            responseList.map { dto ->
                Secret(
                    id = dto.id,
                    userId = dto.userId,
                    encryptedData = symmetricCryptoManager.base64ToBytes(dto.encryptedDataBase64),
                    iv = symmetricCryptoManager.base64ToBytes(dto.ivBase64),
                    salt = symmetricCryptoManager.base64ToBytes(dto.saltBase64),
                    createdAt = dto.createdAt
                )
            }
        }
    }
}