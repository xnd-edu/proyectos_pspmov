package com.example.navigation.domain.usecases.conductores

import com.example.navigation.common.NetworkResult
import com.example.navigation.data.JsonPlaceholderRepository
import com.example.navigation.domain.model.JsonPlaceholderPost
import javax.inject.Inject

class UpdateConductorUseCase @Inject constructor(
    private val jsonPlaceholderRepository: JsonPlaceholderRepository
) {
    suspend operator fun invoke(jsonPlaceholderPost: JsonPlaceholderPost): NetworkResult<JsonPlaceholderPost> = jsonPlaceholderRepository.updatePost(jsonPlaceholderPost)
}