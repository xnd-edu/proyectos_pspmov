package com.example.composeapp.ui.screens.games

sealed interface GamesMainIntent {
    data class SearchGames(val query: String) : GamesMainIntent
    data class OnSearchQueryChange(val query: String) : GamesMainIntent
}

