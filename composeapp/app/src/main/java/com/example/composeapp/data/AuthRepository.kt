package com.example.composeapp.data

import com.example.composeapp.common.HttpStatusCodes
import com.example.composeapp.data.common.NetworkError
import com.example.composeapp.data.local.TokenManager
import com.example.composeapp.data.remote.api.AuthApi
import com.example.composeapp.domain.model.LoginRequest
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val authApi: AuthApi,
    private val tokenManager: TokenManager
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
    suspend fun login(username: String, password: String): Result<Unit> {
        val result = safeApiCall { authApi.login(LoginRequest(username, password)) }

        return result.mapCatching { response ->
            if (!response.success || response.tokens == null) {
                throw NetworkError.Unauthorized()
            }

            val tokens = response.tokens

            // Guardar tokens
            tokenManager.saveAccessToken(tokens.accessToken)
            tokenManager.saveRefreshToken(tokens.refreshToken)

            // Guardar información completa del usuario
            response.usuario?.let { user ->
                tokenManager.saveUserId(user.id)
                tokenManager.saveUsername(user.username)
                tokenManager.saveUserEmail(user.email)
                tokenManager.saveUserNombre(user.nombre)
                user.rol?.let { role ->
                    tokenManager.saveUserRole(role)
                }
            }
        }
    }

    suspend fun logout(): Result<Unit> {
        return try {
            safeApiCall { authApi.logout() }
            tokenManager.clearTokens()
            Result.success(Unit)
        } catch (e: Exception) {
            // Aunque falle, limpiamos tokens localmente
            tokenManager.clearTokens()
            Result.success(Unit)
        }
    }

    fun isLoggedIn(): Boolean = tokenManager.isLoggedIn()
}
