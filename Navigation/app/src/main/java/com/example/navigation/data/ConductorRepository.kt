package com.example.navigation.data

import com.example.navigation.data.local.dao.CocheConductorDao
import com.example.navigation.data.local.dao.ConductoresDao
import com.example.navigation.data.local.entities.CocheConductorCrossRef
import com.example.navigation.data.local.entities.toCoche
import com.example.navigation.data.local.entities.toConductor
import com.example.navigation.data.local.entities.toConductorEntity
import com.example.navigation.domain.model.Coche
import com.example.navigation.domain.model.Conductor
import javax.inject.Inject
import javax.inject.Singleton

class ConductorRepository @Inject constructor(
    private val conductoresDao: ConductoresDao,
    private val cocheConductorDao: CocheConductorDao
) {

    suspend fun getConductores(): List<Conductor> = conductoresDao.getAllConductores().map { it.toConductor() }

    suspend fun getConductor(dni: String): Conductor? = conductoresDao.getConductorByDni(dni)?.toConductor()

    suspend fun getCochesDeConductor(dni: String): List<Coche> {
        return conductoresDao.getConductorConCoches(dni)?.coches?.map { it.toCoche() } ?: emptyList()
    }

    suspend fun asignarCocheAConductor(conductorDni: String, cocheMatricula: String) {
        cocheConductorDao.insertCocheConductor(
            CocheConductorCrossRef(cocheMatricula, conductorDni)
        )
    }

    suspend fun desasignarCocheDeConductor(conductorDni: String, cocheMatricula: String) {
        cocheConductorDao.deleteCocheConductor(
            CocheConductorCrossRef(cocheMatricula, conductorDni)
        )
    }

    suspend fun addConductor(conductor: Conductor) {
        conductoresDao.insertConductor(conductor.toConductorEntity())
    }

    suspend fun updateConductor(conductor: Conductor): Boolean {
        return try {
            conductoresDao.updateConductor(conductor.toConductorEntity())
            true
        } catch (_: Exception) {
            false
        }
    }

    suspend fun deleteConductor(conductor: Conductor): Boolean {
        return try {
            conductoresDao.deleteConductor(conductor.toConductorEntity())
            true
        } catch (_: Exception) {
            false
        }
    }
}