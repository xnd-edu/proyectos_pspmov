package com.example.composeapp.data.remote.api

import com.example.composeapp.common.ApiEndpoints
import com.example.composeapp.domain.model.SecretCreateResponse
import com.example.composeapp.domain.model.SecretRequest
import com.example.composeapp.domain.model.SecretResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface SecretApi {
    @GET(ApiEndpoints.SECRETS)
    suspend fun getSecrets(): List<SecretResponse>

    @GET(ApiEndpoints.SECRET_BY_ID)
    suspend fun getSecretById(@Path(ApiEndpoints.PATH_ID) secretId: Long): SecretResponse

    @POST(ApiEndpoints.SECRETS)
    suspend fun addSecret(@Body secret: SecretRequest): SecretCreateResponse

    @DELETE(ApiEndpoints.SECRET_BY_ID)
    suspend fun deleteSecret(@Path(ApiEndpoints.PATH_ID) secretId: Long)
}