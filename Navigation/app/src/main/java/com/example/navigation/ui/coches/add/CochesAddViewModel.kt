package com.example.navigation.ui.coches.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.navigation.domain.usecases.coches.AddCocheUseCase
import com.example.navigation.R
import com.example.navigation.domain.model.Coche
import com.example.navigation.ui.common.StringProvider
import com.example.navigation.ui.common.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CochesAddViewModel @Inject constructor(
    private val stringProvider: StringProvider,
    private val addCocheUseCase: AddCocheUseCase
) : ViewModel() {
    private var _state: MutableLiveData<CochesAddState> = MutableLiveData(CochesAddState())
    val state: LiveData<CochesAddState> get() = _state

    fun addCoche(coche: Coche) {
        viewModelScope.launch {
            val success = addCocheUseCase(coche)

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

    fun limpiarMensaje() {
        _state.value = _state.value?.copy(event = null)
    }
}
