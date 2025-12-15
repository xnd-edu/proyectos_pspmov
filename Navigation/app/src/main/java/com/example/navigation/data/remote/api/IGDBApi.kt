package com.example.navigation.data.remote.api

import com.example.navigation.common.ApiEndpoints
import com.example.navigation.domain.model.Game
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface IGDBApi {
    @POST(ApiEndpoints.GAMES)
    suspend fun searchGames(@Body query: RequestBody): Response<List<Game>>
}