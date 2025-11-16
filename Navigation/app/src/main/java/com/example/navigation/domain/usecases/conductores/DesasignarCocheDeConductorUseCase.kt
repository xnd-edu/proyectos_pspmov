package com.example.navigation.domain.usecases.conductores

import com.example.navigation.data.ConductorRepository
import javax.inject.Inject

class DesasignarCocheDeConductorUseCase @Inject constructor(
    private val conductorRepository: ConductorRepository
) {
    suspend operator fun invoke(conductorDni: String, cocheMatricula: String) {
        conductorRepository.desasignarCocheDeConductor(conductorDni, cocheMatricula)
    }
}

