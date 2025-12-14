package com.example.navigation.ui.coches.main

import com.example.navigation.domain.model.Game
import com.example.navigation.ui.common.UiEvent

data class CocheMainState(
    val coches: List<Game> = emptyList(),
    val event: UiEvent? = null,
    val isLoading: Boolean = false
)