package com.example.navigation.domain.usecases.jsonplaceholder

import com.example.navigation.common.NetworkResult
import com.example.navigation.data.JsonPlaceholderRepository
import com.example.navigation.domain.model.JsonPlaceholderPost
import javax.inject.Inject

class GetPosts @Inject constructor(
    private val jsonPlaceholderRepository: JsonPlaceholderRepository
) {
    suspend operator fun invoke(): NetworkResult<List<JsonPlaceholderPost>> = jsonPlaceholderRepository.getPosts()
}