package com.example.myapplication.ui.pantalladetalle

import com.example.myapplication.domain.modelo.Coche
import com.example.myapplication.ui.common.UiEvent

data class DetalleState(
    val coche: Coche = Coche(),
    val event: UiEvent? = null
)