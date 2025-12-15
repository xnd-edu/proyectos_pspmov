package com.example.navigation.ui.games.main

import com.example.navigation.domain.model.Game

data class GamesMainState(
    val games: List<Game> = emptyList(),
    val isLoading: Boolean = false
)