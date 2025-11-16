package com.example.navigation.domain.usecases.coches

import com.example.navigation.data.CocheRepository
import com.example.navigation.domain.model.Conductor
import javax.inject.Inject

class GetConductoresDeCocheUseCase @Inject constructor(
    private val cocheRepository: CocheRepository
) {
    suspend operator fun invoke(matricula: String): List<Conductor> {
        return cocheRepository.getConductoresDeCoche(matricula)
    }
}

