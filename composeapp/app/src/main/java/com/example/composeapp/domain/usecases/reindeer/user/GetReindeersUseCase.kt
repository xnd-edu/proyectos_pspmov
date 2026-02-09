package com.example.composeapp.domain.usecases.reindeer.user

import com.example.composeapp.data.ReindeerRepository
import com.example.composeapp.domain.model.Reindeer
import javax.inject.Inject

class GetReindeersUseCase @Inject constructor(
    private val reindeerRepository: ReindeerRepository
) {
    suspend operator fun invoke(): Result<List<Reindeer>> = reindeerRepository.getReindeers()
}