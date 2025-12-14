package com.example.navigation.data

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
    suspend fun getPosts(): NetworkResult<List<JsonPlaceholderPost>> {
        return try {
            val response = api.getPosts()
            NetworkResult.Success(response)
        } catch (e: SocketTimeoutException) {
            NetworkResult.Error("Tiempo de espera agotado. Verifica tu conexión a internet.")
        } catch (e: UnknownHostException) {
            NetworkResult.Error("No se pudo conectar al servidor. Verifica tu conexión a internet.")
        } catch (e: IOException) {
            NetworkResult.Error("Error de red. Por favor, inténtalo de nuevo.")
        } catch (e: HttpException) {
            NetworkResult.Error("Error del servidor (${e.code()}). Inténtalo más tarde.")
        } catch (e: Exception) {
            NetworkResult.Error("Error inesperado: ${e.message ?: "Desconocido"}")
        }
    }

    suspend fun addPost(post: JsonPlaceholderPost): NetworkResult<JsonPlaceholderPost> {
        return try {
            val response = api.addPost(post)
            NetworkResult.Success(response)
        } catch (e: SocketTimeoutException) {
            NetworkResult.Error("Tiempo de espera agotado. Verifica tu conexión a internet.")
        } catch (e: UnknownHostException) {
            NetworkResult.Error("No se pudo conectar al servidor. Verifica tu conexión a internet.")
        } catch (e: IOException) {
            NetworkResult.Error("Error de red. Por favor, inténtalo de nuevo.")
        } catch (e: HttpException) {
            NetworkResult.Error("Error del servidor (${e.code()}). Inténtalo más tarde.")
        } catch (e: Exception) {
            NetworkResult.Error("Error inesperado: ${e.message ?: "Desconocido"}")
        }
    }

    suspend fun getPost(id: Int): NetworkResult<JsonPlaceholderPost> {
        return try {
            val response = api.getPost(id)
            NetworkResult.Success(response)
        } catch (e: SocketTimeoutException) {
            NetworkResult.Error("Tiempo de espera agotado. Verifica tu conexión a internet.")
        } catch (e: UnknownHostException) {
            NetworkResult.Error("No se pudo conectar al servidor. Verifica tu conexión a internet.")
        } catch (e: IOException) {
            NetworkResult.Error("Error de red. Por favor, inténtalo de nuevo.")
        } catch (e: HttpException) {
            NetworkResult.Error("Error del servidor (${e.code()}). Inténtalo más tarde.")
        } catch (e: Exception) {
            NetworkResult.Error("Error inesperado: ${e.message ?: "Desconocido"}")
        }
    }

    suspend fun updatePost(post: JsonPlaceholderPost): NetworkResult<JsonPlaceholderPost> {
        return try {
            val response = api.updatePost(post.id, post)
            NetworkResult.Success(response)
        } catch (e: SocketTimeoutException) {
            NetworkResult.Error("Tiempo de espera agotado. Verifica tu conexión a internet.")
        } catch (e: UnknownHostException) {
            NetworkResult.Error("No se pudo conectar al servidor. Verifica tu conexión a internet.")
        } catch (e: IOException) {
            NetworkResult.Error("Error de red. Por favor, inténtalo de nuevo.")
        } catch (e: HttpException) {
            NetworkResult.Error("Error del servidor (${e.code()}). Inténtalo más tarde.")
        } catch (e: Exception) {
            NetworkResult.Error("Error inesperado: ${e.message ?: "Desconocido"}")
        }
    }

    suspend fun deletePost(id: Int): NetworkResult<Unit> {
        return try {
            api.deletePost(id)
            NetworkResult.Success(Unit)
        } catch (e: SocketTimeoutException) {
            NetworkResult.Error("Tiempo de espera agotado. Verifica tu conexión a internet.")
        } catch (e: UnknownHostException) {
            NetworkResult.Error("No se pudo conectar al servidor. Verifica tu conexión a internet.")
        } catch (e: IOException) {
            NetworkResult.Error("Error de red. Por favor, inténtalo de nuevo.")
        } catch (e: HttpException) {
            NetworkResult.Error("Error del servidor (${e.code()}). Inténtalo más tarde.")
        } catch (e: Exception) {
            NetworkResult.Error("Error inesperado: ${e.message ?: "Desconocido"}")
        }
    }
}