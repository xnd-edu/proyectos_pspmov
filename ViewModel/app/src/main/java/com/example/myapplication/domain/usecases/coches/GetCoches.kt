package com.example.myapplication.domain.usecases.coches

import com.example.myapplication.data.CocheRepository

class GetCoches(){
    operator fun invoke() = CocheRepository.getCoches()
}