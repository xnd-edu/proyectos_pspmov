package com.example.navigation.ui.conductores.main

import com.example.navigation.domain.model.JsonPlaceholderPost

data class ConductoresMainState(
    val conductores: List<JsonPlaceholderPost> = emptyList(),
    val isLoading: Boolean = false
)