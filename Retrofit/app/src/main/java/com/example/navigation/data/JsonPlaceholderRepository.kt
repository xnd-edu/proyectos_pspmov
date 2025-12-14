package com.example.navigation.data

import com.example.navigation.common.NetworkResult
import com.example.navigation.data.remote.api.JsonPlaceholderApi
import com.example.navigation.domain.model.JsonPlaceholderPost
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JsonPlaceholderRepository @Inject constructor(
    private val api: JsonPlaceholderApi
) {
    suspend fun getPosts(): NetworkResult<List<JsonPlaceholderPost>> {
        return try {
            val response = api.getPosts()
            NetworkResult.Success(response)
        } catch (e: HttpException) {
            NetworkResult.Error("Error HTTP ${e.code()}: ${e.message()}")
        }
    }
}