package com.example.navigation.domain.usecases.conductores

import com.example.navigation.common.NetworkResult
import com.example.navigation.data.JsonPlaceholderRepository
import com.example.navigation.domain.model.JsonPlaceholderPost
import javax.inject.Inject

class GetConductorByDniUseCase @Inject constructor(
    private val jsonPlaceholderRepository: JsonPlaceholderRepository
) {
    suspend operator fun invoke(id: Int): NetworkResult<JsonPlaceholderPost> = jsonPlaceholderRepository.getPost(id)
}