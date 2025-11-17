package com.example.navigation.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.navigation.data.local.entities.CocheEntity
import com.example.navigation.data.local.entities.CocheConConductores
import com.example.navigation.data.common.Constants

@Dao
interface CochesDao {
    @Query("SELECT * FROM ${Constants.TABLE_COCHES} ORDER BY ${Constants.COLUMN_MARCA} ASC")
    suspend fun getAllCoches(): List<CocheEntity>

    @Query("SELECT * FROM ${Constants.TABLE_COCHES} WHERE ${Constants.COLUMN_MATRICULA} = :matricula")
    suspend fun getCocheByMatricula(matricula: String): CocheEntity?

    @Transaction
    @Query("SELECT * FROM ${Constants.TABLE_COCHES} WHERE ${Constants.COLUMN_MATRICULA} = :matricula")
    suspend fun getCocheConConductores(matricula: String): CocheConConductores?

    @Insert
    suspend fun insertCoche(coche: CocheEntity)

    @Update
    suspend fun updateCoche(coche: CocheEntity)

    @Delete
    suspend fun deleteCoche(coche: CocheEntity)
}