package com.example.navigation.ui.conductores.add

import com.example.navigation.domain.model.Conductor
import com.example.navigation.ui.common.UiEvent

data class ConductoresAddState(
    val conductor: Conductor? = null,
    val event: UiEvent? = null
)