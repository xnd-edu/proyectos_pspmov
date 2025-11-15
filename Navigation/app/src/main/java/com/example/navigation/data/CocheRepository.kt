package com.example.navigation.data

import com.example.navigation.domain.model.Coche
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CocheRepository @Inject constructor() {

    private val coches = mutableListOf<Coche>()

    init {
        coches.add(
            Coche(
                matricula = "8923BCD",
                marca = "Toyota",
                modelo = "Corolla",
                electrico = false,
                fechaMatriculacion = "15/03/2018",
                color = "Rojo",
                tipo = "Sedan",
                comentarios = "Buen estado general"
            )
        )
        coches.add(Coche(
            matricula = "4567LFG",
            marca = "Tesla",
            modelo = "Model 3",
            electrico = true,
            fechaMatriculacion = "22/07/2020",
            color = "Blanco",
            tipo = "Sedan",
            comentarios = "Batería en excelente estado"
        ))
    }

    fun getCoches(): List<Coche> = coches.toList()

    fun getCoche(id: Int) = coches[id]

    fun addCoche(coche: Coche) = coches.add(coche)

    fun updateCoche(matricula: String, coche: Coche): Boolean {
        val index = coches.indexOfFirst { it.matricula == matricula }
        return if (index != -1) {
            coches[index] = coche
            true
        } else {
            false
        }
    }

    fun deleteCoche(coche: Coche): Boolean {
        val index = coches.indexOfFirst { it.matricula == coche.matricula }
        return if (index != -1) {
            coches.removeAt(index)
            true
        } else {
            false
        }
    }

    fun getSizeList() = coches.size
}