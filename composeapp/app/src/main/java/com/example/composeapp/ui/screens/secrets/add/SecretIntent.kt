package com.example.composeapp.ui.screens.secrets.add

interface SecretIntent {
    data class ChangeSecretText(val text: String) : SecretIntent
    data class ChangePassword(val password: String) : SecretIntent
    data object SaveSecret : SecretIntent
}