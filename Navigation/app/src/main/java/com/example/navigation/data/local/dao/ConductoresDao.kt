package com.example.navigation.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.navigation.data.local.entities.ConductorEntity
import com.example.navigation.data.local.entities.ConductorConCoches
import com.example.navigation.data.common.Constants

@Dao
interface ConductoresDao {
    @Query("SELECT * FROM ${Constants.TABLE_CONDUCTORES} ORDER BY ${Constants.COLUMN_APELLIDOS} ASC")
    suspend fun getAllConductores(): List<ConductorEntity>

    @Query("SELECT * FROM ${Constants.TABLE_CONDUCTORES} WHERE ${Constants.COLUMN_DNI} = :dni")
    suspend fun getConductorByDni(dni: String): ConductorEntity?

    @Transaction
    @Query("SELECT * FROM ${Constants.TABLE_CONDUCTORES} WHERE ${Constants.COLUMN_DNI} = :dni")
    suspend fun getConductorConCoches(dni: String): ConductorConCoches?

    @Insert
    suspend fun insertConductor(conductor: ConductorEntity)

    @Update
    suspend fun updateConductor(conductor: ConductorEntity)

    @Delete
    suspend fun deleteConductor(conductor: ConductorEntity)
}