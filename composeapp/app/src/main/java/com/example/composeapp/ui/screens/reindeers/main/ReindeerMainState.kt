package com.example.composeapp.ui.screens.reindeers.main

import com.example.composeapp.domain.model.Reindeer

data class ReindeerMainState(
    val reindeers: List<Reindeer> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isAdmin: Boolean = false
)