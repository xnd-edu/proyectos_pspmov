package com.example.navigation.ui.conductores.main

import com.example.navigation.domain.model.Conductor

data class ConductoresMainState(
    val conductores: List<Conductor> = emptyList(),
)