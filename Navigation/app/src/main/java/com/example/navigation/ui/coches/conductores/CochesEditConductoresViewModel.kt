package com.example.navigation.ui.coches.conductores

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.navigation.domain.model.Conductor
import com.example.navigation.domain.usecases.coches.AsignarConductorACocheUseCase
import com.example.navigation.domain.usecases.coches.DesasignarConductorDeCocheUseCase
import com.example.navigation.domain.usecases.coches.GetConductoresDeCocheUseCase
import com.example.navigation.domain.usecases.conductores.GetConductores
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CochesEditConductoresViewModel @Inject constructor(
    private val getConductoresDeCoche: GetConductoresDeCocheUseCase,
    private val getTodosConductores: GetConductores,
    private val asignarConductorACoche: AsignarConductorACocheUseCase,
    private val desasignarConductorDeCoche: DesasignarConductorDeCocheUseCase
) : ViewModel() {

    private val _state = MutableLiveData(CochesEditConductoresState())
    val state: LiveData<CochesEditConductoresState> get() = _state

    fun loadConductores(matricula: String) {
        viewModelScope.launch {
            try {
                val conductoresList = getConductoresDeCoche(matricula)
                _state.value = _state.value?.copy(conductores = conductoresList)
            } catch (_: Exception) {
                // Error al cargar jsonplaceholder
            }
        }
    }

    fun getConductoresDisponibles(matricula: String, onResult: (List<Conductor>) -> Unit) {
        viewModelScope.launch {
            try {
                val todosConductores = getTodosConductores()
                val conductoresAsignados = getConductoresDeCoche(matricula)
//                val disponibles = todosConductores.filter { conductor ->
//                    conductoresAsignados.none { it.dni == conductor.dni }
//                }
                onResult(conductoresAsignados)
            } catch (_: Exception) {
                onResult(emptyList())
            }
        }
    }

    fun asignarConductor(matricula: String, dni: String) {
        viewModelScope.launch {
            try {
                asignarConductorACoche(matricula, dni)
                loadConductores(matricula)
            } catch (_: Exception) {
                // Error al asignar conductor
            }
        }
    }

    fun desasignarConductor(matricula: String, dni: String) {
        viewModelScope.launch {
            try {
                desasignarConductorDeCoche(matricula, dni)
                loadConductores(matricula)
            } catch (_: Exception) {
                // Error al desasignar conductor
            }
        }
    }
}