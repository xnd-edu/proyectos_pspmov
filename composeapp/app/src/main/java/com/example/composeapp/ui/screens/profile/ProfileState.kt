package com.example.composeapp.ui.screens.profile

import com.example.composeapp.domain.model.UserDTO

data class ProfileState(
    val user: UserDTO? = null,
    val isLoading: Boolean = false
)

