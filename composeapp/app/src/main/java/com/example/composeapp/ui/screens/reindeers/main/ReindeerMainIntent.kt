package com.example.composeapp.ui.screens.reindeers.main

sealed interface ReindeerMainIntent {
    data object LoadReindeers : ReindeerMainIntent
}

