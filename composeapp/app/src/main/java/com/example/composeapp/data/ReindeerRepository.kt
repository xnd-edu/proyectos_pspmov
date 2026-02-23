package com.example.composeapp.data

import com.example.composeapp.data.common.safeApiCall
import com.example.composeapp.data.remote.api.ReindeerApi
import com.example.composeapp.domain.model.Reindeer
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReindeerRepository @Inject constructor(
    private val api: ReindeerApi
) {
    suspend fun getReindeers(): Result<List<Reindeer>> {
        return safeApiCall { api.getReindeers() }
    }

    suspend fun addReindeer(reindeer: Reindeer): Result<Reindeer> {
        return safeApiCall { api.addReindeer(reindeer) }
    }

    suspend fun getReindeerById(id: Int): Result<Reindeer> {
        return safeApiCall { api.getReindeerById(id) }
    }

    suspend fun updateReindeer(reindeer: Reindeer): Result<Reindeer> {
        return safeApiCall { api.updateReindeer(reindeer.id!!, reindeer) }
    }

    suspend fun deleteReindeer(id: Int): Result<Unit> {
        return safeApiCall { api.deleteReindeer(id) }
    }

    // Admin endpoints
    suspend fun getReindeersAdmin(): Result<List<Reindeer>> {
        return safeApiCall { api.getReindeersAdmin() }
    }

    suspend fun getReindeerByIdAdmin(id: Int): Result<Reindeer> {
        return safeApiCall { api.getReindeerByIdAdmin(id) }
    }

    suspend fun deleteReindeerAdmin(id: Int): Result<Unit> {
        return safeApiCall { api.deleteReindeerAdmin(id) }
    }
}