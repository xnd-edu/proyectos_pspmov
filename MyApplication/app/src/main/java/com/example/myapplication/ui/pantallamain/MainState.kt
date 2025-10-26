package com.example.myapplication.ui.pantallamain

import com.example.myapplication.domain.modelo.Coche

data class MainState(
    val coche: Coche = Coche(),
    val indiceCoche: Int = 0,
    val mensaje: String? = null,
    val sizeList: Int = 0
)