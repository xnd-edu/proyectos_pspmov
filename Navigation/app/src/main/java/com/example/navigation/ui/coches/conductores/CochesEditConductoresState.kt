package com.example.navigation.ui.coches.conductores

import com.example.navigation.domain.model.Conductor

data class CochesEditConductoresState(
    val conductores: List<Conductor> = emptyList(),
)