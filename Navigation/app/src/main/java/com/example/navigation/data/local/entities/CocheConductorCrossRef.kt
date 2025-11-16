package com.example.navigation.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    primaryKeys = ["cocheMatricula", "conductorDni"],
    tableName = "coche_conductor_cross_ref",
    foreignKeys = [
        ForeignKey(
            entity = CocheEntity::class,
            parentColumns = ["matricula"],
            childColumns = ["cocheMatricula"],
            onDelete = ForeignKey.NO_ACTION,
            onUpdate = ForeignKey.NO_ACTION
        ),
        ForeignKey(
            entity = ConductorEntity::class,
            parentColumns = ["dni"],
            childColumns = ["conductorDni"],
            onDelete = ForeignKey.NO_ACTION,
            onUpdate = ForeignKey.NO_ACTION
        )
    ]
)
data class CocheConductorCrossRef(
    val cocheMatricula: String,
    val conductorDni: String
)
