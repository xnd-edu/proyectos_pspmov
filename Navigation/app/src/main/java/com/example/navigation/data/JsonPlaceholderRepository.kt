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

    suspend fun addPost(post: JsonPlaceholderPost): NetworkResult<JsonPlaceholderPost> {
        return try {
            val response = api.addPost(post)
            NetworkResult.Success(response)
        } catch (e: HttpException) {
            NetworkResult.Error("Error HTTP ${e.code()}: ${e.message()}")
        }
    }

    suspend fun getPost(id: Int): NetworkResult<JsonPlaceholderPost> {
        return try {
            val response = api.getPost(id)
            NetworkResult.Success(response)
        } catch (e: HttpException) {
            NetworkResult.Error("Error HTTP ${e.code()}: ${e.message()}")
        }
    }

    suspend fun updatePost(post: JsonPlaceholderPost): NetworkResult<JsonPlaceholderPost> {
        return try {
            val response = api.updatePost(post.id, post)
            NetworkResult.Success(response)
        } catch (e: HttpException) {
            NetworkResult.Error("Error HTTP ${e.code()}: ${e.message()}")
        }
    }

    suspend fun deletePost(id: Int): NetworkResult<Unit> {
        return try {
            api.deletePost(id)
            NetworkResult.Success(Unit)
        } catch (e: HttpException) {
            NetworkResult.Error("Error HTTP ${e.code()}: ${e.message()}")
        }
    }
}