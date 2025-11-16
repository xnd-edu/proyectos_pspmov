package com.example.navigation.ui.conductores.edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.navigation.R
import com.example.navigation.domain.model.Conductor
import com.example.navigation.domain.usecases.conductores.DeleteConductorUseCase
import com.example.navigation.domain.usecases.conductores.GetConductorByDniUseCase
import com.example.navigation.domain.usecases.conductores.UpdateConductorUseCase
import com.example.navigation.ui.common.StringProvider
import com.example.navigation.ui.common.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConductoresEditViewModel @Inject constructor(
    private val stringProvider: StringProvider,
    private val updateConductorUseCase: UpdateConductorUseCase,
    private val deleteConductorUseCase: DeleteConductorUseCase,
    private val getConductor: GetConductorByDniUseCase
) : ViewModel() {
    private var _state: MutableLiveData<ConductoresEditState> = MutableLiveData(ConductoresEditState())
    val state: LiveData<ConductoresEditState> get() = _state
    private var originalDni: String? = null

    fun saveConductor(conductor: Conductor) {
        viewModelScope.launch {
            val success = updateConductorUseCase(conductor)

            if (success) {
                _state.value = _state.value?.copy(
                    event = UiEvent.ShowSnackbar(stringProvider.getString(R.string.conductor_guardado_exito))
                )
                _state.value = _state.value?.copy(
                    event = UiEvent.PopBackStack
                )
            } else {
                _state.value = _state.value?.copy(
                    event = UiEvent.ShowSnackbar(stringProvider.getString(R.string.error_guardar))
                )
            }
        }
    }

    fun deleteConductor(conductor: Conductor) {
        viewModelScope.launch {
            val result = deleteConductorUseCase(conductor)
            if (result) {
                _state.value = _state.value?.copy(
                    event = UiEvent.ShowSnackbar(stringProvider.getString(R.string.conductor_eliminado_exito))
                )
                _state.value = _state.value?.copy(
                    event = UiEvent.PopBackStack
                )
            } else {
                _state.value = _state.value?.copy(
                    event = UiEvent.ShowSnackbar(stringProvider.getString(R.string.error_eliminar))
                )
            }
        }
    }

    fun loadConductor(matricula: String) {
        viewModelScope.launch {
            val conductor = getConductor(matricula)

            if (conductor == null) {
                _state.value = _state.value?.copy(
                    event = UiEvent.ShowSnackbar(stringProvider.getString(R.string.error_get_conductor))
                )
            } else {
                originalDni = conductor.dni
                _state.value = _state.value?.copy(conductor = conductor) ?: ConductoresEditState(conductor)
            }
        }
    }

    fun limpiarMensaje() {
        _state.value = _state.value?.copy(event = null)
    }
}
