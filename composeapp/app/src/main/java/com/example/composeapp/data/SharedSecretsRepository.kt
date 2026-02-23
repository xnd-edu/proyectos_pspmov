package com.example.composeapp.data

import com.example.composeapp.R
import com.example.composeapp.data.common.NetworkError
import com.example.composeapp.data.common.safeApiCall
import com.example.composeapp.data.remote.api.SharedSecretApi
import com.example.composeapp.domain.model.SharedSecretRequest
import com.example.composeapp.ui.common.StringProvider
import javax.inject.Inject

class SharedSecretsRepository @Inject constructor(
    private val api: SharedSecretApi,
    private val stringProvider: StringProvider
) {
    suspend fun shareSecret(
        secretId: Long,
        sharedWithUserId: Long,
        encryptedData: String,
        encryptedKey: String,
        iv: String
    ): Result<Long> {
        val result = safeApiCall { api.shareSecret(SharedSecretRequest(secretId, sharedWithUserId, encryptedData, encryptedKey, iv)) }

        return result.mapCatching { response ->
            if (response.id <= 0) {
                throw NetworkError.Unknown(stringProvider.getString(R.string.error_compartir_secreto))
            }

            response.id
        }
    }

    suspend fun revokeSharedSecret(sharedSecretId: Long): Result<Unit> {
        return safeApiCall { api.deleteSharedSecret(sharedSecretId) }
    }
}