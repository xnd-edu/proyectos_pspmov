package com.example.composeapp.domain.usecases.coches

import com.example.composeapp.data.CocheRepository
import javax.inject.Inject

class GetCocheUseCase @Inject constructor(
    private val cocheRepository: CocheRepository
) {
    operator fun invoke(id: Int) = cocheRepository.getCoche(id)
}