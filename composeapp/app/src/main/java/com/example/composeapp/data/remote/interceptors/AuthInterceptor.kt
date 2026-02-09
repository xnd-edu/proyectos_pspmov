package com.example.composeapp.data.remote.interceptors

import com.example.composeapp.common.ApiConstants
import com.example.composeapp.data.local.TokenManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // Si no hay token, procede sin autorización
        val accessToken = tokenManager.getAccessToken()

        val authenticatedRequest = originalRequest.newBuilder()
            .header(ApiConstants.HEADER_AUTHORIZATION, "${ApiConstants.BEARER_PREFIX}$accessToken")
            .build()

        return chain.proceed(authenticatedRequest)
    }
}

