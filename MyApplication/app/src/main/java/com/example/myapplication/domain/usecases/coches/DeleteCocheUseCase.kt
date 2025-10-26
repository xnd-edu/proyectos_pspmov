package com.example.myapplication.domain.usecases.coches

import com.example.myapplication.data.CocheRepository

class DeleteCocheUseCase {
    operator fun invoke(id: Int): Boolean = CocheRepository.deleteCoche(id)
}