package com.example.myapplication.domain.usecases.coches

import com.example.myapplication.data.CocheRepository
import com.example.myapplication.domain.modelo.Coche

class UpdateCocheUseCase {
    operator fun invoke(matricula: String, coche: Coche): Boolean =
        CocheRepository.updateCoche(matricula, coche)
}