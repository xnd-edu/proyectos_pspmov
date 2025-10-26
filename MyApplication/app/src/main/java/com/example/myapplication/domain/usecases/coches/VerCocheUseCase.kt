package com.example.myapplication.domain.usecases.coches

import com.example.myapplication.data.CocheRepository

class VerCocheUseCase {
    operator fun invoke(id: Int) = CocheRepository.getCoche(id)
}