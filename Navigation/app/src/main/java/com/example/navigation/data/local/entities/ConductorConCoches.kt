package com.example.navigation.data.local.entities

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class ConductorConCoches(
    @Embedded val conductor: ConductorEntity,
    @Relation(
        parentColumn = "dni",
        entityColumn = "matricula",
        associateBy = Junction(
            value = CocheConductorCrossRef::class,
            parentColumn = "conductorDni",
            entityColumn = "cocheMatricula"
        )
    )
    val coches: List<CocheEntity>
)

