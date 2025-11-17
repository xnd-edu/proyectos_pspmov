package com.example.navigation.ui.conductores.edit

import com.example.navigation.domain.model.Conductor
import com.example.navigation.ui.common.UiEvent

data class ConductoresEditState(
    val conductor: Conductor? = null,
    val event: UiEvent? = null
)