package com.example.composeapp.data

import com.example.composeapp.common.HttpStatusCodes
import com.example.composeapp.data.common.NetworkError
import com.example.composeapp.data.remote.api.ReindeerApi
import com.example.composeapp.domain.model.Reindeer
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReindeerRepository @Inject constructor(
    private val api: ReindeerApi
) {
    private suspend fun <T> safeApiCall(apiCall: suspend () -> T): Result<T> {
        return try {
            Result.success(apiCall())
        } catch (e: SocketTimeoutException) {
            Result.failure(NetworkError.Timeout())
        } catch (e: UnknownHostException) {
            Result.failure(NetworkError.NoConnection())
        } catch (e: IOException) {
            Result.failure(NetworkError.Connection())
        } catch (e: HttpException) {
            val error = when (e.code()) {
                HttpStatusCodes.UNAUTHORIZED -> NetworkError.Unauthorized()
                HttpStatusCodes.FORBIDDEN -> NetworkError.Forbidden()
                else -> NetworkError.ServerError(e.code())
            }
            Result.failure(error)
        } catch (e: Exception) {
            Result.failure(NetworkError.Unknown(e.message))
        }
    }

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