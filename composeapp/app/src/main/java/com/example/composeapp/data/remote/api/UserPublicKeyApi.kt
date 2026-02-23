package com.example.composeapp.data.remote.api

import com.example.composeapp.common.ApiEndpoints
import com.example.composeapp.domain.model.UserPublicKeyResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface UserPublicKeyApi {
    @GET(ApiEndpoints.PUBLIC_KEY_BY_USERNAME)
    suspend fun getPublicKeyByUsername(@Path(ApiEndpoints.PATH_USERNAME) username: String): UserPublicKeyResponse
}