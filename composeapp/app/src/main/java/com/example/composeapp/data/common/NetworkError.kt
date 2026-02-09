package com.example.composeapp.data.common

sealed class NetworkError : Exception() {
    class Timeout : NetworkError()
    class NoConnection : NetworkError()
    class Connection : NetworkError()
    class Unauthorized : NetworkError()
    class Forbidden : NetworkError()
    data class ServerError(val code: Int) : NetworkError()
    data class Unknown(override val message: String?) : NetworkError()
}




