package com.example.navigation.domain.usecases.coches

import com.example.navigation.data.CocheRepository
import javax.inject.Inject

class DesasignarConductorDeCocheUseCase @Inject constructor(
    private val cocheRepository: CocheRepository
) {
    suspend operator fun invoke(cocheMatricula: String, conductorDni: String) {
        cocheRepository.desasignarConductorDeCoche(cocheMatricula, conductorDni)
    }
}

