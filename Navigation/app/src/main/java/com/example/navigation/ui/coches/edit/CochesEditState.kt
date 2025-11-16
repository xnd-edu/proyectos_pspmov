package com.example.navigation.ui.coches.edit

import com.example.navigation.domain.model.Coche
import com.example.navigation.ui.common.UiEvent

data class CochesEditState(
    val coche: Coche? = null,
    val event: UiEvent? = null
)