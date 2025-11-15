package com.example.navigation.domain.usecases.coches

import com.example.navigation.data.CocheRepository
import com.example.navigation.domain.model.Coche
import javax.inject.Inject

class UpdateCocheUseCase @Inject constructor(
    private val cocheRepository: CocheRepository
) {
    operator fun invoke(matricula: String, coche: Coche): Boolean =
        cocheRepository.updateCoche(matricula, coche)
}