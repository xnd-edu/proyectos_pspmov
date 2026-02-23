package com.example.composeapp.data

import com.example.composeapp.R
import com.example.composeapp.data.common.NetworkError
import com.example.composeapp.data.common.safeApiCall
import com.example.composeapp.data.remote.api.SecretApi
import com.example.composeapp.domain.model.SecretRequest
import com.example.composeapp.domain.model.SecretResponse
import com.example.composeapp.ui.common.StringProvider
import javax.inject.Inject

class SecretsRepository @Inject constructor(
    private val api: SecretApi,
    private val stringProvider: StringProvider
) {
    suspend fun getSecrets(): Result<List<SecretResponse>> {
        return safeApiCall { api.getSecrets() }
    }

    suspend fun getSecretById(secretId: Long): Result<SecretResponse> {
        return safeApiCall { api.getSecretById(secretId) }
    }

    suspend fun addSecret(encryptedSecret: String, iv: String, salt: String): Result<Long> {
        val result = safeApiCall { api.addSecret(SecretRequest(encryptedSecret, iv, salt)) }

        return result.mapCatching { response ->
            if (response.id <= 0) {
                throw NetworkError.Unknown(stringProvider.getString(R.string.error_guardar_secreto))
            }

            response.id
        }
    }

    suspend fun deleteSecret(secretId: Long): Result<Unit> {
        return safeApiCall { api.deleteSecret(secretId) }
    }
}