package com.example.composeapp.ui.coches

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composeapp.R
import com.example.composeapp.domain.modelo.Coche
import com.example.composeapp.domain.usecases.coches.AddCocheUseCase
import com.example.composeapp.domain.usecases.coches.DeleteCocheUseCase
import com.example.composeapp.domain.usecases.coches.GetCocheUseCase
import com.example.composeapp.domain.usecases.coches.GetListSizeUseCase
import com.example.composeapp.domain.usecases.coches.UpdateCocheUseCase
import com.example.composeapp.ui.common.StringProvider
import com.example.composeapp.ui.common.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CocheViewModel @Inject constructor(
    private val stringProvider: StringProvider,
    private val addCocheUseCase: AddCocheUseCase,
    private val deleteCocheUseCase: DeleteCocheUseCase,
    private val updateCocheUseCase: UpdateCocheUseCase,
    private val getCocheUseCase: GetCocheUseCase,
    private val getListSizeUseCase: GetListSizeUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(CocheState())
    val state: StateFlow<CocheState> = _state.asStateFlow()
    private val _events = Channel<UiEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    init {
        viewModelScope.launch {
            val sizeList = getListSizeUseCase()
            if (sizeList > 0) {
                loadCoche(0)
                _state.update { it.copy(sizeList = sizeList) }
            }
        }
    }

    fun handleIntent(intent: CocheIntent) {
        when (intent) {
            is CocheIntent.IrCocheAnterior -> irCocheAnterior()
            is CocheIntent.IrCocheSiguiente -> irCocheSiguiente()
            is CocheIntent.ChangeCoche -> updateCocheState(intent.coche)
            is CocheIntent.SaveCoche -> saveCoche()
            is CocheIntent.UpdateCoche -> updateCoche()
            is CocheIntent.DeleteCoche -> deleteCoche()
            is CocheIntent.LimpiarFormulario -> limpiarFormulario()
        }
    }

    private fun irCocheAnterior() {
        viewModelScope.launch {
            val indiceActual = _state.value.indiceCoche
            if (indiceActual > 0) {
                loadCoche(indiceActual - 1)
            } else {
                sendEvent(UiEvent.ShowSnackbar(message = stringProvider.getString(R.string.msg_no_coches_anteriores)))
            }
        }
    }

    private fun irCocheSiguiente() {
        viewModelScope.launch {
            val indiceActual = _state.value.indiceCoche
            val sizeList = _state.value.sizeList
            if (indiceActual < sizeList - 1) {
                loadCoche(indiceActual + 1)
            } else {
                sendEvent(UiEvent.ShowSnackbar(message = stringProvider.getString(R.string.msg_no_mas_coches)))
            }
        }
    }

    private fun loadCoche(indice: Int) {
        viewModelScope.launch {
            val coche = getCocheUseCase(indice)
            _state.update {
                it.copy(
                    coche = coche,
                    indiceCoche = indice
                )
            }
        }
    }

    private fun saveCoche() {
        viewModelScope.launch {
            val coche = _state.value.coche

            val success = addCocheUseCase(coche)

            if (success) {
                val newSize = getListSizeUseCase()
                _state.update { it.copy(sizeList = newSize) }
                loadCoche(newSize - 1)

                sendEvent(UiEvent.ShowSnackbar(stringProvider.getString(R.string.msg_coche_guardado)))
            } else {
                sendEvent(UiEvent.ShowSnackbar(stringProvider.getString(R.string.msg_error_guardar_coche)))
            }
        }
    }

    private fun updateCoche() {
        viewModelScope.launch {
            val coche = _state.value.coche
            val matricula = coche.matricula

            if (matricula.isNullOrEmpty()) {
                sendEvent(UiEvent.ShowSnackbar(stringProvider.getString(R.string.msg_matricula_vacia)))
                return@launch
            }

            val success = updateCocheUseCase(coche.matricula, coche)

            if (success) {
                val newSize = getListSizeUseCase()
                _state.update { it.copy(sizeList = newSize) }

                sendEvent(UiEvent.ShowSnackbar(stringProvider.getString(R.string.msg_coche_actualizado)))
            } else {
                sendEvent(UiEvent.ShowSnackbar(stringProvider.getString(R.string.msg_error_actualizar_coche)))
            }
        }
    }

    private fun deleteCoche() {
        viewModelScope.launch {
            val coche = _state.value.coche

            val success = deleteCocheUseCase(coche)

            if (success) {
                val newSize = getListSizeUseCase()
                _state.update { it.copy(sizeList = newSize) }

                if (newSize > 0) {
                    val indiceActual = _state.value.indiceCoche
                    val nuevoIndice = if (indiceActual >= newSize) newSize - 1 else indiceActual
                    loadCoche(nuevoIndice)
                } else {
                    limpiarFormulario()
                }

                sendEvent(UiEvent.ShowSnackbar(stringProvider.getString(R.string.msg_coche_eliminado)))
            } else {
                sendEvent(UiEvent.ShowSnackbar(stringProvider.getString(R.string.msg_error_eliminar_coche)))
            }
        }
    }

    private fun limpiarFormulario() {
        val cocheVacio = Coche(
            matricula = "",
            marca = "",
            modelo = "",
            electrico = false,
            fechaMatriculacion = "",
            color = "",
            tipo = "",
            comentarios = ""
        )
        _state.update { it.copy(coche = cocheVacio) }
    }

    private fun updateCocheState(coche: Coche) {
        _state.update { it.copy(coche = coche) }
    }

    private fun sendEvent(event: UiEvent) {
        viewModelScope.launch {
            _events.send(event)
        }
    }
}