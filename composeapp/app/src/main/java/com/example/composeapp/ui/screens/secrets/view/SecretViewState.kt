package com.example.composeapp.ui.screens.secrets.view

import com.example.composeapp.domain.model.SecretDecrypted

data class SecretViewState(
    val secret: SecretDecrypted? = null,
    val isLoading: Boolean = false,
    val usernameInput: String = "",
    val isShareDialogVisible: Boolean = false
)