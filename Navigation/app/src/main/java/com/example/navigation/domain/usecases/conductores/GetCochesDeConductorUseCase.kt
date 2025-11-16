package com.example.navigation.domain.usecases.conductores

import com.example.navigation.data.ConductorRepository
import com.example.navigation.domain.model.Coche
import javax.inject.Inject

class GetCochesDeConductorUseCase @Inject constructor(
    private val conductorRepository: ConductorRepository
) {
    suspend operator fun invoke(dni: String): List<Coche> {
        return conductorRepository.getCochesDeConductor(dni)
    }
}

