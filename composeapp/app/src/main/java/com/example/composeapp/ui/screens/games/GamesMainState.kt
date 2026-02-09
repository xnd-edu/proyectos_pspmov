package com.example.composeapp.ui.screens.games

import com.example.composeapp.domain.model.Game

data class GamesMainState(
    val games: List<Game> = emptyList(),
    val isLoading: Boolean = false,
    val searchQuery: String = ""
)

