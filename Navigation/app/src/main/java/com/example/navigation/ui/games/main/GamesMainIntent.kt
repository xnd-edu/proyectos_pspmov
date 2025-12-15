package com.example.navigation.ui.games.main

interface GamesMainIntent {
    data class SearchGames(val query: String) : GamesMainIntent
}