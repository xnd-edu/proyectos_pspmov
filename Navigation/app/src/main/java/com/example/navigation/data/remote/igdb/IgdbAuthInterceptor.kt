package com.example.navigation.data.remote.igdb

import okhttp3.Interceptor
import okhttp3.Response

class IgdbAuthInterceptor(
    private val clientId: String,
    private val accessToken: String
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val authenticatedRequest = chain.request().newBuilder()
            .header("Client-ID", clientId)
            .header("Authorization", "Bearer $accessToken")
            .build()

        return chain.proceed(authenticatedRequest)
    }
}

