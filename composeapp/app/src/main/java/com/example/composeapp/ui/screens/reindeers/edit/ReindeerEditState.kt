package com.example.composeapp.ui.screens.reindeers.edit

import com.example.composeapp.domain.model.Reindeer

data class ReindeerEditState(
    val reindeer: Reindeer? = null,
    val isLoading: Boolean = false,
    val isAdmin: Boolean = false
)