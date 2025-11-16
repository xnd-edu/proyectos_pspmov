package com.example.navigation.domain.usecases.conductores

import com.example.navigation.data.ConductorRepository
import javax.inject.Inject

class AsignarCocheAConductorUseCase @Inject constructor(
    private val conductorRepository: ConductorRepository
) {
    suspend operator fun invoke(conductorDni: String, cocheMatricula: String) {
        conductorRepository.asignarCocheAConductor(conductorDni, cocheMatricula)
    }
}

