package com.example.composeapp.data

import com.example.composeapp.data.common.NetworkError
import com.example.composeapp.data.common.safeApiCall
import com.example.composeapp.data.local.TokenManager
import com.example.composeapp.data.remote.api.AuthApi
import com.example.composeapp.domain.model.LoginRequest
import com.example.composeapp.domain.model.RegisterRequest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val authApi: AuthApi,
    private val tokenManager: TokenManager
) {
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
        } catch (_: Exception) {
            // Aunque falle, limpiamos tokens localmente
            tokenManager.clearTokens()
            Result.success(Unit)
        }
    }

    suspend fun register(username: String, password: String, email: String, nombre: String, publicKey: String): Result<Unit> {
        val result = safeApiCall { authApi.register(RegisterRequest(username, email, password, nombre, publicKey)) }

        return result.mapCatching { response ->
            if (!response.success || response.tokens == null) {
                throw NetworkError.BadRequest(response.message)
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

    fun isLoggedIn(): Boolean = tokenManager.isLoggedIn()
}
