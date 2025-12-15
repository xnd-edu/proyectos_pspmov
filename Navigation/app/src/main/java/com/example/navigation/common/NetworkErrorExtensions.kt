package com.example.navigation.common

import com.example.navigation.R
import com.example.navigation.ui.common.StringProvider

fun NetworkError.toMessage(stringProvider: StringProvider): String {
    return when (this) {
        NetworkError.Timeout -> stringProvider.getString(R.string.error_timeout)
        NetworkError.NoConnection -> stringProvider.getString(R.string.error_no_connection)
        NetworkError.Connection -> stringProvider.getString(R.string.error_network)
        is NetworkError.ServerError -> stringProvider.getString(R.string.error_server, code)
        is NetworkError.Unknown -> stringProvider.getString(R.string.error_unknown, message ?: "")
    }
}

