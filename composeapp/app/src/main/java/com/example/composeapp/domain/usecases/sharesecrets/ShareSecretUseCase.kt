package com.example.composeapp.domain.usecases.sharesecrets

import com.example.composeapp.data.SharedSecretsRepository
import com.example.composeapp.data.UserPublicKeyRepository
import com.example.composeapp.data.security.AsymmetricCryptoManager
import com.example.composeapp.data.security.ServerVerifier
import com.example.composeapp.data.security.SymmetricCryptoManager
import javax.inject.Inject

class ShareSecretUseCase @Inject constructor(
    private val sharedSecretsRepository: SharedSecretsRepository,
    private val userPublicKeyRepository: UserPublicKeyRepository,
    private val symmetricCryptoManager: SymmetricCryptoManager,
    private val asymmetricCryptoManager: AsymmetricCryptoManager,
    private val serverVerifier: ServerVerifier
) {
    suspend operator fun invoke(secretId: Long, username: String, secretText: String): Result<Long> {
        val result = userPublicKeyRepository.getUserPublicKey(username)

        return result.mapCatching { response ->
            val validSignature = serverVerifier.isUserKeyTrusted(
                response.userId,
                response.publicKeyBase64,
                response.createdAt.toString(),
                response.serverSignatureBase64
            )

            check(validSignature)

            val key = symmetricCryptoManager.generateRandomKey()
            val encryptionResult = symmetricCryptoManager.encryptGCM(secretText, key)
            val encryptedKey = asymmetricCryptoManager.encryptOAEP(key.encoded, asymmetricCryptoManager.base64ToPublicKey(response.publicKeyBase64))

            return sharedSecretsRepository.shareSecret(
                secretId,
                response.userId,
                encryptionResult.encryptedData,
                encryptedKey,
                encryptionResult.iv
            )
        }
    }
}

