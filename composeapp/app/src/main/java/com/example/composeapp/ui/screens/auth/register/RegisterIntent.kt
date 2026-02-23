package com.example.composeapp.ui.screens.auth.register

sealed interface RegisterIntent {
    data class OnUsernameChange(val username: String) : RegisterIntent
    data class OnEmailChange(val email: String) : RegisterIntent
    data class OnPasswordChange(val password: String) : RegisterIntent
    data class OnNombreChange(val nombre: String) : RegisterIntent
    data object OnRegisterClick : RegisterIntent
}

