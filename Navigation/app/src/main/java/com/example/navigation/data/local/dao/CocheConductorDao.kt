package com.example.navigation.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.navigation.data.local.entities.CocheConductorCrossRef

@Dao
interface CocheConductorDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCocheConductor(crossRef: CocheConductorCrossRef)

    @Delete
    suspend fun deleteCocheConductor(crossRef: CocheConductorCrossRef)

    @Query("DELETE FROM coche_conductor_cross_ref WHERE cocheMatricula = :matricula")
    suspend fun deleteTodosConductoresDeCoche(matricula: String)

    @Query("DELETE FROM coche_conductor_cross_ref WHERE conductorDni = :dni")
    suspend fun deleteTodosCochesDeConductor(dni: String)

    @Query("SELECT * FROM coche_conductor_cross_ref WHERE cocheMatricula = :matricula")
    suspend fun getConductoresPorCoche(matricula: String): List<CocheConductorCrossRef>

    @Query("SELECT * FROM coche_conductor_cross_ref WHERE conductorDni = :dni")
    suspend fun getCochesPorConductor(dni: String): List<CocheConductorCrossRef>
}

