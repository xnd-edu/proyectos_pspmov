package com.example.navigation.data

import com.example.navigation.common.NetworkResult
import com.example.navigation.data.remote.api.IGDBApi
import com.example.navigation.domain.model.Game
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IGDBRepository @Inject constructor(
    private val api: IGDBApi
) {
    suspend fun searchGames(searchQuery: String, limit: Int = 10): NetworkResult<List<Game>> {
        return try {
            val query = "search \"$searchQuery\"; fields name, first_release_date, summary, rating, cover.image_id; limit $limit;"
            val requestBody = query.toRequestBody("text/plain".toMediaTypeOrNull())
            val response = api.searchGames(requestBody)

            if (response.isSuccessful) {
                NetworkResult.Success(response.body() ?: emptyList())
            } else {
                NetworkResult.Error("Error del servidor (${response.code()}). Inténtalo más tarde.")
            }
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

