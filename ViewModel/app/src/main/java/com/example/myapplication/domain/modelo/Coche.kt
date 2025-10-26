package com.example.myapplication.domain.modelo

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Coche(
    val matricula: String? = null,
    val marca: String? = null,
    val modelo: String? = null,
    val electrico: Boolean? = null,
    val fechaMatriculacion: String? = null,
    val color: String? = null,
    val tipo: String? = null,
    val comentarios: String? = null
) : Parcelable