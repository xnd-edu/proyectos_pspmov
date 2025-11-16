package com.example.navigation.ui.conductores.coches

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.navigation.domain.model.Coche
import com.example.navigation.domain.usecases.coches.GetCoches
import com.example.navigation.domain.usecases.conductores.AsignarCocheAConductorUseCase
import com.example.navigation.domain.usecases.conductores.DesasignarCocheDeConductorUseCase
import com.example.navigation.domain.usecases.conductores.GetCochesDeConductorUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConductoresEditCochesViewModel @Inject constructor(
    private val getCochesDeConductor: GetCochesDeConductorUseCase,
    private val getTodosCoches: GetCoches,
    private val asignarCocheAConductor: AsignarCocheAConductorUseCase,
    private val desasignarCocheDeConductor: DesasignarCocheDeConductorUseCase
) : ViewModel() {

    private val _state = MutableLiveData(ConductoresEditCochesState())
    val state: LiveData<ConductoresEditCochesState> get() = _state

    fun loadCoches(dni: String) {
        viewModelScope.launch {
            try {
                val cochesList = getCochesDeConductor(dni)
                _state.value = _state.value?.copy(coches = cochesList)
            } catch (_: Exception) {
                // Error al cargar coches
            }
        }
    }

    fun getCochesDisponibles(dni: String, onResult: (List<Coche>) -> Unit) {
        viewModelScope.launch {
            try {
                val todosCoches = getTodosCoches()
                val cochesAsignados = getCochesDeConductor(dni)
                val disponibles = todosCoches.filter { coche ->
                    cochesAsignados.none { it.matricula == coche.matricula }
                }
                onResult(disponibles)
            } catch (_: Exception) {
                onResult(emptyList())
            }
        }
    }

    fun asignarCoche(dni: String, matricula: String) {
        viewModelScope.launch {
            try {
                asignarCocheAConductor(dni, matricula)
                loadCoches(dni)
            } catch (_: Exception) {
                // Error al asignar coche
            }
        }
    }

    fun desasignarCoche(dni: String, matricula: String) {
        viewModelScope.launch {
            try {
                desasignarCocheDeConductor(dni, matricula)
                loadCoches(dni)
            } catch (_: Exception) {
                // Error al desasignar coche
            }
        }
    }
}

