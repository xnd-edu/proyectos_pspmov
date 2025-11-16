package com.example.navigation.domain.usecases.coches

import com.example.navigation.data.CocheRepository
import com.example.navigation.domain.model.Coche
import javax.inject.Inject

class UpdateCocheUseCase @Inject constructor(
    private val cocheRepository: CocheRepository
) {
    suspend operator fun invoke(coche: Coche): Boolean = cocheRepository.updateCoche(coche)
}