package com.example.navigation.domain.model

data class Conductor(
    val dni: String,
    val nombre: String? = null,
    val apellidos: String? = null,
    val telefono: String? = null,
    val fechaNacimiento: String? = null,
    val genero: String? = null
)