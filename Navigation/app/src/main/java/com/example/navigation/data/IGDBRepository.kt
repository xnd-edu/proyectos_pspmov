package com.example.navigation.data

import com.example.navigation.common.ApiConstants
import com.example.navigation.common.IgdbQueryConstants
import com.example.navigation.common.NetworkError
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
            val query = String.format(IgdbQueryConstants.SEARCH_QUERY_TEMPLATE, searchQuery, limit)
            val requestBody = query.toRequestBody(ApiConstants.CONTENT_TYPE_TEXT_PLAIN.toMediaTypeOrNull())
            val response = api.searchGames(requestBody)

            if (response.isSuccessful) {
                NetworkResult.Success(response.body() ?: emptyList())
            } else {
                NetworkResult.Error(NetworkError.ServerError(response.code()))
            }
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
}

