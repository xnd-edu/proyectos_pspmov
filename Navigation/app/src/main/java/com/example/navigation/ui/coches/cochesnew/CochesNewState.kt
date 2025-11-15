package com.example.navigation.ui.coches.cochesnew

import com.example.navigation.domain.model.Coche
import com.example.navigation.ui.common.UiEvent

data class CochesNewState(
    val coche: Coche = Coche(),
    val event: UiEvent? = null
)