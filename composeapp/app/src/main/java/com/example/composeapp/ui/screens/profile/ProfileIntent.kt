package com.example.composeapp.ui.screens.profile

sealed interface ProfileIntent {
    data object Logout : ProfileIntent
}

