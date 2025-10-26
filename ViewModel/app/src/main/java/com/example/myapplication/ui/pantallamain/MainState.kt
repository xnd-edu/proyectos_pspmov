package com.example.myapplication.ui.pantallamain

import com.example.myapplication.domain.modelo.Coche

data class MainState(
    val coches: List<Coche> = emptyList(),
    val isIrDetalle: Boolean = false,
)