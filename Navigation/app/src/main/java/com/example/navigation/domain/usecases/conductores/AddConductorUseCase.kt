package com.example.navigation.domain.usecases.conductores

import com.example.navigation.data.ConductorRepository
import com.example.navigation.domain.model.Conductor
import javax.inject.Inject

class AddConductorUseCase @Inject constructor(
    private val conductorRepository: ConductorRepository
) {
    suspend operator fun invoke(conductor: Conductor): Boolean {
        return try {
            conductorRepository.addConductor(conductor)
            true
        } catch (e: Exception) {
            false
        }
    }
}