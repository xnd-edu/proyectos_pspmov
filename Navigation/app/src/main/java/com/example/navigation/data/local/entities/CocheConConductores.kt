package com.example.navigation.data.local.entities

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.navigation.data.common.Constants

data class CocheConConductores(
    @Embedded val coche: CocheEntity,
    @Relation(
        parentColumn = Constants.COLUMN_MATRICULA,
        entityColumn = Constants.COLUMN_DNI,
        associateBy = Junction(
            value = CocheConductorCrossRef::class,
            parentColumn = Constants.COLUMN_COCHE_MATRICULA,
            entityColumn = Constants.COLUMN_CONDUCTOR_DNI
        )
    )
    val conductores: List<ConductorEntity>
)

