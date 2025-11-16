package com.example.navigation.data.local.entities

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class CocheConConductores(
    @Embedded val coche: CocheEntity,
    @Relation(
        parentColumn = "matricula",
        entityColumn = "dni",
        associateBy = Junction(
            value = CocheConductorCrossRef::class,
            parentColumn = "cocheMatricula",
            entityColumn = "conductorDni"
        )
    )
    val conductores: List<ConductorEntity>
)

