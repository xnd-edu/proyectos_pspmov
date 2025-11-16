package com.example.navigation.domain.usecases.coches

import com.example.navigation.data.CocheRepository
import com.example.navigation.domain.model.Coche
import javax.inject.Inject

class AddCocheUseCase @Inject constructor(
    private val cocheRepository: CocheRepository
) {
    suspend operator fun invoke(coche: Coche): Boolean {
        return try {
            cocheRepository.addCoche(coche)
            true
        } catch (_: Exception) {
            false
        }
    }
}