package com.example.composeapp.ui.screens.secrets.view

sealed interface SecretViewIntent {
    data object DeleteSecret : SecretViewIntent
    data object ShareSecret : SecretViewIntent
    data object RevokeSecret : SecretViewIntent
    data class OnUsernameChange(val username: String) : SecretViewIntent
    object OnConfirmShare : SecretViewIntent
    object OnDismissDialog : SecretViewIntent
}
