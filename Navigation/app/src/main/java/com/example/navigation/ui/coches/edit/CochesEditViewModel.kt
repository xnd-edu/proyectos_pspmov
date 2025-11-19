package com.example.navigation.ui.coches.edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.navigation.domain.usecases.coches.DeleteCocheUseCase
import com.example.navigation.R
import com.example.navigation.domain.model.Coche
import com.example.navigation.domain.usecases.coches.GetCocheByMatriculaUseCase
import com.example.navigation.domain.usecases.coches.UpdateCocheUseCase
import com.example.navigation.ui.common.StringProvider
import com.example.navigation.ui.common.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CochesEditViewModel @Inject constructor(
    private val stringProvider: StringProvider,
    private val updateCocheUseCase: UpdateCocheUseCase,
    private val deleteCocheUseCase: DeleteCocheUseCase,
    private val getCoche: GetCocheByMatriculaUseCase
) : ViewModel() {
    private var _state: MutableLiveData<CochesEditState> = MutableLiveData(CochesEditState())
    val state: LiveData<CochesEditState> get() = _state

    fun saveCoche(coche: Coche) {
        viewModelScope.launch {
            val success = updateCocheUseCase(coche)

            if (success) {
                _state.value = _state.value?.copy(
                    event = UiEvent.ShowSnackbar(stringProvider.getString(R.string.coche_guardado_exito))
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

    fun deleteCoche(coche: Coche) {
        viewModelScope.launch {
            val result = deleteCocheUseCase(coche)
            if (result) {
                _state.value = _state.value?.copy(
                    event = UiEvent.ShowSnackbar(stringProvider.getString(R.string.coche_eliminado_exito))
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

    fun loadCoche(matricula: String) {
        viewModelScope.launch {
            val coche = getCoche(matricula)

            if (coche == null) {
                _state.value = _state.value?.copy(
                    event = UiEvent.ShowSnackbar(stringProvider.getString(R.string.error_get_coche))
                )
            } else {
                _state.value = _state.value?.copy(coche = coche) ?: CochesEditState(coche)
            }
        }
    }

    fun limpiarEvento() {
        _state.value = _state.value?.copy(event = null)
    }
}
