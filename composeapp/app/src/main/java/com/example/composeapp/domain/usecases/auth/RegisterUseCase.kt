package com.example.composeapp.domain.usecases.auth

import com.example.composeapp.R
import com.example.composeapp.data.AuthRepository
import com.example.composeapp.data.security.AsymmetricCryptoManager
import com.example.composeapp.ui.common.StringProvider
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val asymmetricCryptoManager: AsymmetricCryptoManager,
    private val stringProvider: StringProvider
) {
    suspend operator fun invoke(
        username: String,
        email: String,
        password: String,
        nombre: String
    ): Result<Unit> {
        asymmetricCryptoManager.generateAndStoreKeyPair()

        val publicKey = asymmetricCryptoManager.getMyPublicKey()
            ?: return Result.failure(Exception(stringProvider.getString(R.string.error_clave_publica)))
        val publicKeyBase64 = asymmetricCryptoManager.publicKeyToBase64(publicKey)

        return authRepository.register(username, password, email, nombre, publicKeyBase64)
    }
}

