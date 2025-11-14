package com.example.navigation.ui.coches.cochesmain

import com.example.navigation.domain.model.Coche

data class CocheMainState(
    val coches: List<Coche> = emptyList(),
)