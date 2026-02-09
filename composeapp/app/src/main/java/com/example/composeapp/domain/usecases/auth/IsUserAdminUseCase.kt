package com.example.composeapp.domain.usecases.auth

import com.example.composeapp.data.local.TokenManager
import javax.inject.Inject

class IsUserAdminUseCase @Inject constructor(
    private val tokenManager: TokenManager
) {
    operator fun invoke(): Boolean {
        return tokenManager.isAdmin()
    }
}

