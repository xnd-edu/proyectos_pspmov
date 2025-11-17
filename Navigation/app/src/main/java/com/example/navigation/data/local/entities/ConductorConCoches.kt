package com.example.navigation.data.local.entities

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.navigation.data.common.Constants

data class ConductorConCoches(
    @Embedded val conductor: ConductorEntity,
    @Relation(
        parentColumn = Constants.COLUMN_DNI,
        entityColumn = Constants.COLUMN_MATRICULA,
        associateBy = Junction(
            value = CocheConductorCrossRef::class,
            parentColumn = Constants.COLUMN_CONDUCTOR_DNI,
            entityColumn = Constants.COLUMN_COCHE_MATRICULA
        )
    )
    val coches: List<CocheEntity>
)

