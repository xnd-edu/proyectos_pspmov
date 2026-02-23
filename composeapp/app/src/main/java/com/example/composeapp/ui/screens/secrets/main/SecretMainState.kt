package com.example.composeapp.ui.screens.secrets.main

import com.example.composeapp.domain.model.Secret

data class SecretMainState(
    val secrets: List<Secret> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedSecretId: Long? = null, // Si no es null, el diálogo se muestra
    val passwordInput: String = ""
)