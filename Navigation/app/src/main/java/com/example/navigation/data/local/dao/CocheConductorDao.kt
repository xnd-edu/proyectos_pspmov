package com.example.navigation.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.navigation.data.local.entities.CocheConductorCrossRef
import com.example.navigation.data.common.Constants

@Dao
interface CocheConductorDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCocheConductor(crossRef: CocheConductorCrossRef)

    @Delete
    suspend fun deleteCocheConductor(crossRef: CocheConductorCrossRef)

    @Query("DELETE FROM ${Constants.TABLE_COCHE_CONDUCTOR_CROSS_REF} WHERE ${Constants.COLUMN_COCHE_MATRICULA} = :matricula")
    suspend fun deleteTodosConductoresDeCoche(matricula: String)

    @Query("DELETE FROM ${Constants.TABLE_COCHE_CONDUCTOR_CROSS_REF} WHERE ${Constants.COLUMN_CONDUCTOR_DNI} = :dni")
    suspend fun deleteTodosCochesDeConductor(dni: String)

    @Query("SELECT * FROM ${Constants.TABLE_COCHE_CONDUCTOR_CROSS_REF} WHERE ${Constants.COLUMN_COCHE_MATRICULA} = :matricula")
    suspend fun getConductoresPorCoche(matricula: String): List<CocheConductorCrossRef>

    @Query("SELECT * FROM ${Constants.TABLE_COCHE_CONDUCTOR_CROSS_REF} WHERE ${Constants.COLUMN_CONDUCTOR_DNI} = :dni")
    suspend fun getCochesPorConductor(dni: String): List<CocheConductorCrossRef>
}

