package com.example.composeapp.ui.screens.reindeers.edit

import com.example.composeapp.domain.model.Reindeer

sealed interface ReindeerEditIntent {
    data class ChangeReindeer(val reindeer: Reindeer) : ReindeerEditIntent
    data object SaveReindeer : ReindeerEditIntent
    data object DeleteReindeer : ReindeerEditIntent
}
