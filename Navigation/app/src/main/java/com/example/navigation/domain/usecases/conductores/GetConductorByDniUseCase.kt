package com.example.navigation.domain.usecases.conductores

import com.example.navigation.data.ConductorRepository
import com.example.navigation.domain.model.Conductor
import javax.inject.Inject

class GetConductorByDniUseCase @Inject constructor(
    private val conductorRepository: ConductorRepository
) {
    suspend operator fun invoke(dni: String): Conductor? = conductorRepository.getConductor(dni)
}