package com.example.composeapp.data.common

import com.example.composeapp.common.HttpStatusCodes
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Utilidad para llamadas seguras a la API, usada por todos los repositorios.
 */
suspend fun <T> safeApiCall(apiCall: suspend () -> T): Result<T> {
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