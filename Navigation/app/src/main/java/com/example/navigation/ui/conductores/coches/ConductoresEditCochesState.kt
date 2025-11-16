package com.example.navigation.ui.conductores.coches

import com.example.navigation.domain.model.Coche

data class ConductoresEditCochesState(
    val coches: List<Coche> = emptyList()
)

