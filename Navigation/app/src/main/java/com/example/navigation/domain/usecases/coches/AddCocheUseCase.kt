package com.example.myapplication.domain.usecases.coches

import com.example.navigation.data.CocheRepository
import com.example.navigation.domain.model.Coche
import javax.inject.Inject

class AddCocheUseCase @Inject constructor(
    private val cocheRepository: CocheRepository
) {
    operator fun invoke(coche: Coche): Boolean = cocheRepository.addCoche(coche)
}