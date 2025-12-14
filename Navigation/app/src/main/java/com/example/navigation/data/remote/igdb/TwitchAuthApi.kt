package com.example.navigation.data.remote.igdb

import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Query

interface TwitchAuthApi {
    @POST("oauth2/token")
    suspend fun getAccessToken(
        @Query("client_id") clientId: String,
        @Query("client_secret") clientSecret: String,
        @Query("grant_type") grantType: String = "client_credentials"
    ): Response<TwitchAuthResponse>
}

