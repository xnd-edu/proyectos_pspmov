package com.example.composeapp.data

import com.example.composeapp.data.common.safeApiCall
import com.example.composeapp.data.remote.api.UserPublicKeyApi
import com.example.composeapp.domain.model.UserPublicKeyResponse
import javax.inject.Inject

class UserPublicKeyRepository @Inject constructor(
    private val api: UserPublicKeyApi
) {
    suspend fun getUserPublicKey(username: String): Result<UserPublicKeyResponse> {
        return safeApiCall { api.getPublicKeyByUsername(username) }
    }
}