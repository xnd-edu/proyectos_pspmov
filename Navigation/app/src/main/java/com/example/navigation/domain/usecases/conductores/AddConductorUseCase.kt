package com.example.navigation.domain.usecases.conductores

import com.example.navigation.data.ConductorRepository
import javax.inject.Inject

class AddConductorUseCase @Inject constructor(
    private val conductorRepository: ConductorRepository
) {
    suspend operator fun invoke(conductor: com.example.navigation.domain.model.Conductor): Boolean {
        return try {
            conductorRepository.addConductor(conductor)
            true
        } catch (e: Exception) {
            false
        }
    }
}