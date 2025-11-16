package com.example.navigation.domain.model

data class Coche(
    val matricula: String,
    val marca: String? = null,
    val modelo: String? = null,
    val electrico: Boolean? = null,
    val fechaMatriculacion: String? = null,
    val color: String? = null,
    val tipo: String? = null,
    val comentarios: String? = null
)