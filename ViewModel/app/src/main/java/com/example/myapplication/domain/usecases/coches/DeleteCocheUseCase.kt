package com.example.myapplication.domain.usecases.coches

import com.example.myapplication.data.CocheRepository
import com.example.myapplication.domain.modelo.Coche

class DeleteCocheUseCase {
    operator fun invoke(coche: Coche): Boolean = CocheRepository.deleteCoche(coche)
}