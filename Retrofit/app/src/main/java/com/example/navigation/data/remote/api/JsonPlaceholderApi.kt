package com.example.navigation.data.remote.api

import com.example.navigation.domain.model.JsonPlaceholderPost
import retrofit2.http.GET

interface JsonPlaceholderApi {
    @GET("posts")
    suspend fun getPosts(): List<JsonPlaceholderPost>
}