package com.example.navigation.ui.coches.add

import com.example.navigation.domain.model.Coche
import com.example.navigation.ui.common.UiEvent

data class CochesAddState(
    val coche: Coche? = null,
    val event: UiEvent? = null
)