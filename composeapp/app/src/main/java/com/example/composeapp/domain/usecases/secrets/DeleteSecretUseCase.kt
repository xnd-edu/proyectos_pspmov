package com.example.composeapp.domain.usecases.secrets

import com.example.composeapp.data.SecretsRepository
import javax.inject.Inject

class DeleteSecretUseCase @Inject constructor(
    private val secretsRepository: SecretsRepository
) {
    suspend operator fun invoke(id: Long): Result<Unit> = secretsRepository.deleteSecret(id)
}

