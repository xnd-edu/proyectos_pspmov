package com.example.navigation.ui.coches.main

import com.example.navigation.domain.model.Coche

data class CocheMainState(
    val coches: List<Coche> = emptyList(),
)