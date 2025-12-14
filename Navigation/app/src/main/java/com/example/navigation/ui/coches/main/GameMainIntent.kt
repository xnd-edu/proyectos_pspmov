package com.example.navigation.ui.coches.main

interface GameMainIntent {
    data class SearchGames(val query: String) : GameMainIntent
}