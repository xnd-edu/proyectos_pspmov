package com.example.composeapp.data.security

import com.example.composeapp.common.CryptoConstants
import java.security.PublicKey

class ServerVerifier(
    private val asymmetricManager: AsymmetricCryptoManager,
    private val serverPublicKey: PublicKey
) {

    fun isUserKeyTrusted(
        userId: Long,
        userPublicKeyBase64: String,
        signedAt: String,
        serverSignatureBase64: String
    ): Boolean {

        val messageToCheck = String.format(
            CryptoConstants.SIGNATURE_FORMAT,
            userId,
            userPublicKeyBase64,
            signedAt
        )

        return asymmetricManager.verifySignature(
            message = messageToCheck,
            signatureBase64 = serverSignatureBase64,
            serverPublicKey = serverPublicKey
        )
    }
}