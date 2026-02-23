package com.example.composeapp.data.common

import com.example.composeapp.R
import com.example.composeapp.ui.common.StringProvider
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkErrorMapper @Inject constructor(
    private val stringProvider: StringProvider
) {
    fun toMessage(error: NetworkError): String {
        return when (error) {
            is NetworkError.Timeout -> stringProvider.getString(R.string.error_timeout)
            is NetworkError.NoConnection -> stringProvider.getString(R.string.error_no_connection)
            is NetworkError.Connection -> stringProvider.getString(R.string.error_network)
            is NetworkError.Unauthorized -> stringProvider.getString(R.string.error_unauthorized)
            is NetworkError.Forbidden -> stringProvider.getString(R.string.error_forbidden)
            is NetworkError.BadRequest -> stringProvider.getString(R.string.error_bad_request, error.message ?: "")
            is NetworkError.ServerError -> stringProvider.getString(R.string.error_server, error.code)
            is NetworkError.Unknown -> stringProvider.getString(R.string.error_unknown, error.message ?: "")
        }
    }
}
