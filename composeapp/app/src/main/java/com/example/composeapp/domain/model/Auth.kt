package com.example.composeapp.domain.model

data class LoginRequest(
    val username: String,
    val password: String
)

data class LoginResponse(
    val success: Boolean,
    val message: String,
    val usuario: UserDTO?,
    val tokens: TokenResponse?
)

data class TokenResponse(
    val accessToken: String,
    val refreshToken: String,
    val tokenType: String
)

data class UserDTO(
    val id: Int,
    val username: String,
    val email: String?,
    val nombre: String?,
    val rol: String?
)
