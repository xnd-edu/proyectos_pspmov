package com.example.composeapp.data.remote.api

import com.example.composeapp.common.ApiEndpoints
import com.example.composeapp.domain.model.Reindeer
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ReindeerApi {
    @GET(ApiEndpoints.REINDEERS)
    suspend fun getReindeers(): List<Reindeer>

    @GET(ApiEndpoints.REINDEERS_ADMIN)
    suspend fun getReindeersAdmin(): List<Reindeer>

    @GET(ApiEndpoints.REINDEER_BY_ID)
    suspend fun getReindeerById(@Path(ApiEndpoints.PATH_ID) id: Int): Reindeer

    @GET(ApiEndpoints.REINDEER_BY_ID_ADMIN)
    suspend fun getReindeerByIdAdmin(@Path(ApiEndpoints.PATH_ID) id: Int): Reindeer

    @POST(ApiEndpoints.REINDEERS_ADMIN)
    suspend fun addReindeer(@Body reindeer: Reindeer): Reindeer

    @PUT(ApiEndpoints.REINDEER_BY_ID_ADMIN)
    suspend fun updateReindeer(@Path(ApiEndpoints.PATH_ID) id: Int, @Body reindeer: Reindeer): Reindeer

    @DELETE(ApiEndpoints.REINDEER_BY_ID)
    suspend fun deleteReindeer(@Path(ApiEndpoints.PATH_ID) id: Int)

    @DELETE(ApiEndpoints.REINDEER_BY_ID_ADMIN)
    suspend fun deleteReindeerAdmin(@Path(ApiEndpoints.PATH_ID) id: Int)
}