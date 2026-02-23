package com.example.composeapp.domain.usecases.sharesecrets

import com.example.composeapp.data.SharedSecretsRepository
import javax.inject.Inject

class RevokeSharedSecretUseCase @Inject constructor(
    private val sharedSecretsRepository: SharedSecretsRepository
) {
    suspend operator fun invoke(id: Long): Result<Unit> = sharedSecretsRepository.revokeSharedSecret(id)
}

