package com.example.navigation.ui.conductores.add

import com.example.navigation.domain.model.JsonPlaceholderPost

interface JsonPlaceholderAddIntent {
    data class AddConductor(val jsonPlaceholderPost: JsonPlaceholderPost): JsonPlaceholderAddIntent
    data object LimpiarMensaje : JsonPlaceholderAddIntent
}