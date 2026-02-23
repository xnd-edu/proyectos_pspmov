package com.example.composeapp.ui.screens.secrets.main

sealed interface SecretMainIntent {
    data object LoadSecrets : SecretMainIntent
    data class OnSecretClick(val id: Long) : SecretMainIntent
    data class OnPasswordChange(val password: String) : SecretMainIntent
    object OnDismissDialog : SecretMainIntent
    object OnConfirmPassword : SecretMainIntent
}

