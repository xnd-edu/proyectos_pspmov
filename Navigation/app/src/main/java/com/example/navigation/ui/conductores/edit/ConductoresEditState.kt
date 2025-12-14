package com.example.navigation.ui.conductores.edit

import com.example.navigation.domain.model.JsonPlaceholderPost
import com.example.navigation.ui.common.UiEvent

data class ConductoresEditState(
    val conductor: JsonPlaceholderPost? = null,
    val event: UiEvent? = null
)