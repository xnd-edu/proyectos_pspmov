package com.example.navigation.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.navigation.domain.model.Coche

@Entity(tableName = "coches")
data class CocheEntity(
    @PrimaryKey
    val matricula: String,
    val marca: String? = null,
    val modelo: String? = null,
    val electrico: Boolean? = null,
    val fechaMatriculacion: String? = null,
    val color: String? = null,
    val tipo: String? = null,
    val comentarios: String? = null
)

fun CocheEntity.toCoche(): Coche {
    return Coche(
        matricula = this.matricula,
        marca = this.marca,
        modelo = this.modelo,
        electrico = this.electrico,
        fechaMatriculacion = this.fechaMatriculacion,
        color = this.color,
        tipo = this.tipo,
        comentarios = this.comentarios
    )
}

fun Coche.toCocheEntity(): CocheEntity {
    return CocheEntity(
        matricula = this.matricula,
        marca = this.marca,
        modelo = this.modelo,
        electrico = this.electrico,
        fechaMatriculacion = this.fechaMatriculacion,
        color = this.color,
        tipo = this.tipo,
        comentarios = this.comentarios
    )
}