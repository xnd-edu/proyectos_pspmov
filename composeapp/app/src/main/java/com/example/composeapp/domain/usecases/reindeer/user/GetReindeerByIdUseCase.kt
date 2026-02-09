package com.example.composeapp.domain.usecases.reindeer.user

import com.example.composeapp.data.ReindeerRepository
import com.example.composeapp.domain.model.Reindeer
import javax.inject.Inject

class GetReindeerByIdUseCase @Inject constructor(
    private val reindeerRepository: ReindeerRepository
) {
    suspend operator fun invoke(id: Int): Result<Reindeer> = reindeerRepository.getReindeerById(id)
}