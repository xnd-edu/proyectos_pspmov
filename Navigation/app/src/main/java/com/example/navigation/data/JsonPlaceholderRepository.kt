package com.example.navigation.data

import com.example.navigation.common.NetworkError
import com.example.navigation.common.NetworkResult
import com.example.navigation.data.remote.api.JsonPlaceholderApi
import com.example.navigation.domain.model.JsonPlaceholderPost
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JsonPlaceholderRepository @Inject constructor(
    private val api: JsonPlaceholderApi
) {
    private suspend fun <T> safeApiCall(apiCall: suspend () -> T): NetworkResult<T> {
        return try {
            NetworkResult.Success(apiCall())
        } catch (e: SocketTimeoutException) {
            NetworkResult.Error(NetworkError.Timeout)
        } catch (e: UnknownHostException) {
            NetworkResult.Error(NetworkError.NoConnection)
        } catch (e: IOException) {
            NetworkResult.Error(NetworkError.Connection)
        } catch (e: HttpException) {
            NetworkResult.Error(NetworkError.ServerError(e.code()))
        } catch (e: Exception) {
            NetworkResult.Error(NetworkError.Unknown(e.message))
        }
    }

    suspend fun getPosts(): NetworkResult<List<JsonPlaceholderPost>> {
        return safeApiCall { api.getPosts() }
    }

    suspend fun addPost(post: JsonPlaceholderPost): NetworkResult<JsonPlaceholderPost> {
        return safeApiCall { api.addPost(post) }
    }

    suspend fun getPost(id: Int): NetworkResult<JsonPlaceholderPost> {
        return safeApiCall { api.getPost(id) }
    }

    suspend fun updatePost(post: JsonPlaceholderPost): NetworkResult<JsonPlaceholderPost> {
        return safeApiCall { api.updatePost(post.id, post) }
    }

    suspend fun deletePost(id: Int): NetworkResult<Unit> {
        return safeApiCall { api.deletePost(id) }
    }
}