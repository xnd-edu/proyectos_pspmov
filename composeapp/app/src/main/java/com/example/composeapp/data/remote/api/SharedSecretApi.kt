package com.example.composeapp.data.remote.api

import com.example.composeapp.common.ApiEndpoints
import com.example.composeapp.domain.model.SecretCreateResponse
import com.example.composeapp.domain.model.SharedSecretRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.Path

interface SharedSecretApi {
    @POST(ApiEndpoints.SHARED_SECRETS)
    suspend fun shareSecret(@Body sharedSecret: SharedSecretRequest): SecretCreateResponse

    @DELETE(ApiEndpoints.SHARED_SECRET_BY_ID)
    suspend fun deleteSharedSecret(@Path(ApiEndpoints.PATH_ID) sharedSecretId: Long)
}