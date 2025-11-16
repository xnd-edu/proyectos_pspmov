package com.example.navigation.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.navigation.domain.model.Conductor

@Entity(tableName = "conductores")
data class ConductorEntity(
    @PrimaryKey
    val dni: String,
    val nombre: String? = null,
    val apellidos: String? = null,
    val telefono: String? = null,
    val fechaNacimiento: String? = null,
    val genero: String? = null
)

fun ConductorEntity.toConductor(): Conductor {
    return Conductor(
        dni = this.dni,
        nombre = this.nombre,
        apellidos = this.apellidos,
        telefono = this.telefono,
        fechaNacimiento = this.fechaNacimiento,
        genero = this.genero
    )
}

fun Conductor.toConductorEntity(): ConductorEntity {
    return ConductorEntity(
        dni = this.dni,
        nombre = this.nombre,
        apellidos = this.apellidos,
        telefono = this.telefono,
        fechaNacimiento = this.fechaNacimiento,
        genero = this.genero
    )
}