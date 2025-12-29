package com.example.composeapp.domain.usecases.coches

import com.example.composeapp.data.CocheRepository
import javax.inject.Inject

class GetListSizeUseCase @Inject constructor(
    private val cocheRepository: CocheRepository
) {
    operator fun invoke() = cocheRepository.getSizeList()
}