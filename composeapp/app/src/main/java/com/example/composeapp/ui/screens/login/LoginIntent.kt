package com.example.composeapp.ui.screens.login

sealed interface LoginIntent {
    data class OnUsernameChange(val username: String) : LoginIntent
    data class OnPasswordChange(val password: String) : LoginIntent
    data object OnLoginClick : LoginIntent
}

