package com.example.composeapp.domain.usecases.reindeer.user

import com.example.composeapp.data.ReindeerRepository
import javax.inject.Inject

class DeleteReindeerUseCase @Inject constructor(
    private val reindeerRepository: ReindeerRepository
) {
    suspend operator fun invoke(id: Int): Result<Unit> = reindeerRepository.deleteReindeer(id)
}