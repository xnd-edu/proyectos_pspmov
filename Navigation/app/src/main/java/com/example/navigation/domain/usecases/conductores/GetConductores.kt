package com.example.navigation.domain.usecases.conductores

import com.example.navigation.data.ConductorRepository
import com.example.navigation.domain.model.Conductor
import javax.inject.Inject

class GetConductores @Inject constructor(
    private val conductorRepository: ConductorRepository
) {
    suspend operator fun invoke(): List<Conductor> = conductorRepository.getConductores()
}