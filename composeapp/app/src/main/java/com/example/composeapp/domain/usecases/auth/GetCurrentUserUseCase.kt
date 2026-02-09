package com.example.composeapp.domain.usecases.auth

import com.example.composeapp.data.local.TokenManager
import com.example.composeapp.domain.model.UserDTO
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(
    private val tokenManager: TokenManager
) {
    operator fun invoke(): UserDTO? {
        val userId = tokenManager.getUserId() ?: return null
        val username = tokenManager.getUsername() ?: return null

        return UserDTO(
            id = userId,
            username = username,
            email = tokenManager.getUserEmail(),
            nombre = tokenManager.getUserNombre(),
            rol = tokenManager.getUserRole()
        )
    }
}


