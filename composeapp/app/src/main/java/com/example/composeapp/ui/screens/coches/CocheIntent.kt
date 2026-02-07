package com.example.composeapp.ui.screens.coches

import com.example.composeapp.domain.modelo.Coche

interface CocheIntent {
    data object DeleteCoche : CocheIntent
    data object IrCocheAnterior : CocheIntent
    data object IrCocheSiguiente : CocheIntent
    data object UpdateCoche : CocheIntent
    data class ChangeCoche(val coche: Coche) : CocheIntent
    data object SaveCoche : CocheIntent
    data object LimpiarFormulario : CocheIntent
}