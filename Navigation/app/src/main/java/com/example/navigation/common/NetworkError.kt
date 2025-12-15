package com.example.navigation.common

sealed class NetworkError {
    object Timeout : NetworkError()
    object NoConnection : NetworkError()
    object Connection : NetworkError()
    data class ServerError(val code: Int) : NetworkError()
    data class Unknown(val message: String?) : NetworkError()
}

