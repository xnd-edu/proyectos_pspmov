package com.example.navigation.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.navigation.data.local.entities.CocheEntity
import com.example.navigation.data.local.entities.CocheConConductores

@Dao
interface CochesDao {
    @Query("SELECT * FROM coches ORDER BY marca ASC")
    suspend fun getAllCoches(): List<CocheEntity>

    @Query("SELECT * FROM coches WHERE matricula = :matricula")
    suspend fun getCocheByMatricula(matricula: String): CocheEntity?

    @Transaction
    @Query("SELECT * FROM coches WHERE matricula = :matricula")
    suspend fun getCocheConConductores(matricula: String): CocheConConductores?

    @Transaction
    @Query("SELECT * FROM coches ORDER BY marca ASC")
    suspend fun getTodosLosCochesConConductores(): List<CocheConConductores>


    @Insert
    suspend fun insertCoche(coche: CocheEntity)

    @Update
    suspend fun updateCoche(coche: CocheEntity)

    @Delete
    suspend fun deleteCoche(coche: CocheEntity)
}