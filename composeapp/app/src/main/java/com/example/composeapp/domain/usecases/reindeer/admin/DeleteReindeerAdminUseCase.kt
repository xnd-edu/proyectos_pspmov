package com.example.composeapp.domain.usecases.reindeer.admin

import com.example.composeapp.data.ReindeerRepository
import com.example.composeapp.data.common.NetworkError
import com.example.composeapp.data.local.TokenManager
import javax.inject.Inject

class DeleteReindeerAdminUseCase @Inject constructor(
    private val reindeerRepository: ReindeerRepository,
    private val tokenManager: TokenManager
) {
    suspend operator fun invoke(id: Int): Result<Unit> {
        // Verificar que el usuario sea ADMIN
        if (!tokenManager.isAdmin()) {
            return Result.failure(NetworkError.Forbidden())
        }

        return reindeerRepository.deleteReindeerAdmin(id)
    }
}

