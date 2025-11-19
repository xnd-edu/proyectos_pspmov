package com.example.navigation.data

import com.example.navigation.data.local.dao.CocheConductorDao
import com.example.navigation.data.local.dao.CochesDao
import com.example.navigation.data.local.entities.CocheConductorCrossRef
import com.example.navigation.data.local.entities.toCoche
import com.example.navigation.data.local.entities.toCocheEntity
import com.example.navigation.data.local.entities.toConductor
import com.example.navigation.domain.model.Coche
import com.example.navigation.domain.model.Conductor
import javax.inject.Inject
import javax.inject.Singleton

class CocheRepository @Inject constructor(
    private val cochesDao: CochesDao,
    private val cocheConductorDao: CocheConductorDao
) {

    suspend fun getCoches(): List<Coche> = cochesDao.getAllCoches().map { it.toCoche() }

    suspend fun getCoche(matricula: String): Coche? = cochesDao.getCocheByMatricula(matricula)?.toCoche()

    suspend fun getConductoresDeCoche(matricula: String): List<Conductor> {
        return cochesDao.getCocheConConductores(matricula)?.conductores?.map { it.toConductor() } ?: emptyList()
    }

    suspend fun asignarConductorACoche(cocheMatricula: String, conductorDni: String) {
        cocheConductorDao.insertCocheConductor(
            CocheConductorCrossRef(cocheMatricula, conductorDni)
        )
    }

    suspend fun desasignarConductorDeCoche(cocheMatricula: String, conductorDni: String) {
        cocheConductorDao.deleteCocheConductor(
            CocheConductorCrossRef(cocheMatricula, conductorDni)
        )
    }

    suspend fun addCoche(coche: Coche) {
        cochesDao.insertCoche(coche.toCocheEntity())
    }

    suspend fun updateCoche(coche: Coche): Boolean {
        return try {
            cochesDao.updateCoche(coche.toCocheEntity())
            true
        } catch (_: Exception) {
            false
        }
    }

    suspend fun deleteCoche(coche: Coche): Boolean {
        return try {
            cochesDao.deleteCoche(coche.toCocheEntity())
            true
        } catch (_: Exception) {
            false
        }
    }
}