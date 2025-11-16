package com.example.navigation.domain.usecases.coches

import com.example.navigation.data.CocheRepository
import com.example.navigation.domain.model.Coche
import javax.inject.Inject

class GetCoches @Inject constructor(
    private val cocheRepository: CocheRepository
) {
    suspend operator fun invoke(): List<Coche> = cocheRepository.getCoches()
}