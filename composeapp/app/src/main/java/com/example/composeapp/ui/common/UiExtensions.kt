package com.example.composeapp.ui.common

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Formatea una fecha LocalDateTime a "dd/MM/yyyy HH:mm".
 */
fun LocalDateTime?.toFormattedString(): String {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
    return this?.format(formatter) ?: "Fecha desconocida"
}