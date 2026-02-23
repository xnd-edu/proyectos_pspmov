package com.example.composeapp.data

import com.example.composeapp.common.ApiConstants
import com.example.composeapp.common.HttpStatusCodes
import com.example.composeapp.common.IgdbQueryConstants
import com.example.composeapp.data.common.NetworkError
import com.example.composeapp.data.common.safeApiCall
import com.example.composeapp.data.remote.api.IGDBApi
import com.example.composeapp.domain.model.Game
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.lang.String.format
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IGDBRepository @Inject constructor(
    private val api: IGDBApi
) {
    suspend fun searchGames(searchQuery: String, limit: Int = IgdbQueryConstants.DEFAULT_SEARCH_LIMIT): Result<List<Game>> {
        val query = format(IgdbQueryConstants.SEARCH_QUERY_TEMPLATE, searchQuery, limit)
        val requestBody = query.toRequestBody(ApiConstants.CONTENT_TYPE_TEXT_PLAIN.toMediaTypeOrNull())
        return safeApiCall {
            val response = api.searchGames(requestBody)
            if (response.isSuccessful) {
                response.body() ?: emptyList()
            } else {
                val error = when (response.code()) {
                    HttpStatusCodes.UNAUTHORIZED -> NetworkError.Unauthorized()
                    HttpStatusCodes.FORBIDDEN -> NetworkError.Forbidden()
                    else -> NetworkError.ServerError(response.code())
                }
                throw error
            }
        }
    }
}
