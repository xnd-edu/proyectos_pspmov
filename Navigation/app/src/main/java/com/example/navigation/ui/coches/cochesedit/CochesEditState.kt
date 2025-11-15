package com.example.navigation.ui.coches.cochesedit

import com.example.navigation.domain.model.Coche
import com.example.navigation.ui.common.UiEvent

data class CochesEditState(
    val coche: Coche = Coche(),
    val event: UiEvent? = null
)