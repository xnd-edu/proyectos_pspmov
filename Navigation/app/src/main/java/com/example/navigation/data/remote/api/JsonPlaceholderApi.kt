package com.example.navigation.data.remote.api

import com.example.navigation.domain.model.JsonPlaceholderPost
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface JsonPlaceholderApi {
    @GET("posts")
    suspend fun getPosts(): List<JsonPlaceholderPost>

    @GET("posts/{id}")
    suspend fun getPost(@Path("id") id: Int): JsonPlaceholderPost

    @POST("posts")
    suspend fun addPost(@Body post: JsonPlaceholderPost): JsonPlaceholderPost

    @PUT("posts/{id}")
    suspend fun updatePost(@Path("id") id: Int, @Body post: JsonPlaceholderPost): JsonPlaceholderPost

    @DELETE("posts/{id}")
    suspend fun deletePost(@Path("id") id: Int)
}