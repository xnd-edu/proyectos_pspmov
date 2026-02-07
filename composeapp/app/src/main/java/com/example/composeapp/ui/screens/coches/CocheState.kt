package com.example.composeapp.ui.screens.coches

import com.example.composeapp.domain.modelo.Coche
import com.example.composeapp.ui.common.UiEvent

data class CocheState(
    val coche: Coche = Coche(),
    val indiceCoche: Int = 0,
    val event: UiEvent? = null,
    val sizeList: Int = 0
)