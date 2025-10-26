package com.example.myapplication.domain.usecases.coches

import com.example.myapplication.data.CocheRepository

class GetListSizeUseCase {
    operator fun invoke() = CocheRepository.getSizeList()
}