package com.example.navigation.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import com.example.navigation.data.common.Constants

@Entity(
    primaryKeys = [Constants.COLUMN_COCHE_MATRICULA, Constants.COLUMN_CONDUCTOR_DNI],
    tableName = Constants.TABLE_COCHE_CONDUCTOR_CROSS_REF,
    foreignKeys = [
        ForeignKey(
            entity = CocheEntity::class,
            parentColumns = [Constants.COLUMN_MATRICULA],
            childColumns = [Constants.COLUMN_COCHE_MATRICULA],
            onDelete = ForeignKey.NO_ACTION,
            onUpdate = ForeignKey.NO_ACTION
        ),
        ForeignKey(
            entity = ConductorEntity::class,
            parentColumns = [Constants.COLUMN_DNI],
            childColumns = [Constants.COLUMN_CONDUCTOR_DNI],
            onDelete = ForeignKey.NO_ACTION,
            onUpdate = ForeignKey.NO_ACTION
        )
    ]
)
data class CocheConductorCrossRef(
    val cocheMatricula: String,
    val conductorDni: String
)
