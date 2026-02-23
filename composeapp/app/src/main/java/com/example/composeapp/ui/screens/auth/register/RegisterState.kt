package com.example.composeapp.ui.screens.auth.register

data class RegisterState(
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val nombre: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val registerSuccess: Boolean = false
)

