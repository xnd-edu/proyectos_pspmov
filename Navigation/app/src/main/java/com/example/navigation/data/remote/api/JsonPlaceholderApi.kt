package com.example.navigation.data.remote.api

import com.example.navigation.common.ApiEndpoints
import com.example.navigation.domain.model.JsonPlaceholderPost
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface JsonPlaceholderApi {
    @GET(ApiEndpoints.POSTS)
    suspend fun getPosts(): List<JsonPlaceholderPost>

    @GET(ApiEndpoints.POSTS_BY_ID)
    suspend fun getPost(@Path(ApiEndpoints.PATH_ID) id: Int): JsonPlaceholderPost

    @POST(ApiEndpoints.POSTS)
    suspend fun addPost(@Body post: JsonPlaceholderPost): JsonPlaceholderPost

    @PUT(ApiEndpoints.POSTS_BY_ID)
    suspend fun updatePost(@Path(ApiEndpoints.PATH_ID) id: Int, @Body post: JsonPlaceholderPost): JsonPlaceholderPost

    @DELETE(ApiEndpoints.POSTS_BY_ID)
    suspend fun deletePost(@Path(ApiEndpoints.PATH_ID) id: Int)
}