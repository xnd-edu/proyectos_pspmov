package com.example.composeapp.domain.usecases.auth

import com.example.composeapp.data.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(username: String, password: String): Result<Unit> {
        return authRepository.login(username, password)
    }
}

