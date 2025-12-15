package com.example.navigation.domain.usecases.jsonplaceholder

import com.example.navigation.common.NetworkResult
import com.example.navigation.data.JsonPlaceholderRepository
import com.example.navigation.domain.model.JsonPlaceholderPost
import javax.inject.Inject

class UpdatePostUseCase @Inject constructor(
    private val jsonPlaceholderRepository: JsonPlaceholderRepository
) {
    suspend operator fun invoke(jsonPlaceholderPost: JsonPlaceholderPost): NetworkResult<JsonPlaceholderPost> = jsonPlaceholderRepository.updatePost(jsonPlaceholderPost)
}