package com.example.composeapp.domain.usecases.coches

import com.example.composeapp.data.CocheRepository
import com.example.composeapp.domain.modelo.Coche
import javax.inject.Inject

class DeleteCocheUseCase @Inject constructor(
    private val cocheRepository: CocheRepository
) {
    operator fun invoke(coche: Coche): Boolean = cocheRepository.deleteCoche(coche)
}