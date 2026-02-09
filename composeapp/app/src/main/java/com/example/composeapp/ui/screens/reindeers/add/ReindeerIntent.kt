package com.example.composeapp.ui.screens.reindeers.add

import com.example.composeapp.domain.model.Reindeer

interface ReindeerIntent {
    data class ChangeReindeer(val reindeer: Reindeer) : ReindeerIntent
    data object SaveReindeer : ReindeerIntent
}