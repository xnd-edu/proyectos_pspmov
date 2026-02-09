package com.example.composeapp.data.remote.interceptors

import com.example.composeapp.common.ApiConstants
import okhttp3.Interceptor
import okhttp3.Response

class IgdbAuthInterceptor(
    private val clientId: String,
    private val accessToken: String
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val authenticatedRequest = chain.request().newBuilder()
            .header(ApiConstants.HEADER_CLIENT_ID, clientId)
            .header(ApiConstants.HEADER_AUTHORIZATION, "${ApiConstants.BEARER_PREFIX}$accessToken")
            .build()

        return chain.proceed(authenticatedRequest)
    }
}

