package com.example.navigation.ui.conductores.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.navigation.R
import com.example.navigation.domain.model.Conductor
import com.example.navigation.domain.usecases.conductores.AddConductorUseCase
import com.example.navigation.ui.common.StringProvider
import com.example.navigation.ui.common.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConductoresAddViewModel @Inject constructor(
    private val stringProvider: StringProvider,
    private val addConductorUseCase: AddConductorUseCase
) : ViewModel() {
    private var _state: MutableLiveData<ConductoresAddState> = MutableLiveData(ConductoresAddState())
    val state: LiveData<ConductoresAddState> get() = _state

    fun addConductor(conductor: Conductor) {
        viewModelScope.launch {
            val success = addConductorUseCase(conductor)

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

    fun limpiarMensaje() {
        _state.value = _state.value?.copy(event = null)
    }
}
