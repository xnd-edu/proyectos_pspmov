package com.example.composeapp.data.remote.api

import com.example.composeapp.common.ApiEndpoints
import com.example.composeapp.domain.model.LoginRequest
import com.example.composeapp.domain.model.LoginResponse
import com.example.composeapp.domain.model.RegisterRequest
import com.example.composeapp.domain.model.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST(ApiEndpoints.AUTH_LOGIN)
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @POST(ApiEndpoints.AUTH_LOGOUT)
    suspend fun logout()

    @POST(ApiEndpoints.AUTH_REGISTER)
    suspend fun register(@Body request: RegisterRequest): RegisterResponse
}

