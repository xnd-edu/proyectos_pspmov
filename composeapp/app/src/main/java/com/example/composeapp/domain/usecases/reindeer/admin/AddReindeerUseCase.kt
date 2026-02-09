package com.example.composeapp.domain.usecases.reindeer.admin

import com.example.composeapp.data.ReindeerRepository
import com.example.composeapp.data.common.NetworkError
import com.example.composeapp.data.local.TokenManager
import com.example.composeapp.domain.model.Reindeer
import javax.inject.Inject

class AddReindeerUseCase @Inject constructor(
    private val reindeerRepository: ReindeerRepository,
    private val tokenManager: TokenManager
) {
    suspend operator fun invoke(reindeer: Reindeer): Result<Reindeer> {
        // Verificar que el usuario sea ADMIN
        if (!tokenManager.isAdmin()) {
            return Result.failure(NetworkError.Forbidden())
        }

        return reindeerRepository.addReindeer(reindeer)
    }
}

