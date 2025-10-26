package com.example.myapplication.ui.pantalladetalle

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.domain.modelo.Coche
import com.example.myapplication.domain.usecases.coches.*
import com.example.myapplication.ui.Constantes
import com.example.myapplication.ui.common.StringProvider
import com.example.myapplication.ui.common.UiEvent

class DetalleViewModel(
    private val stringProvider: StringProvider,
    private val addCocheUseCase: AddCocheUseCase,
    private val updateCocheUseCase: UpdateCocheUseCase,
    private val deleteCocheUseCase: DeleteCocheUseCase,
    private val getCoches: GetCoches
) : ViewModel() {
    private var _state: MutableLiveData<DetalleState> = MutableLiveData(DetalleState(Coche()))
    val state: LiveData<DetalleState> get() = _state
    private var originalMatricula: String? = null

    fun saveCoche(coche: Coche) {
        val success = if (originalMatricula != null) {
            updateCocheUseCase(originalMatricula!!, coche)
        } else {
            addCocheUseCase(coche)
        }

        if (success) {
            _state.value = _state.value?.copy(
                event = UiEvent.ShowSnackbar(Constantes.COCHE_GUARDADO_EXITO)
            )
            _state.value = _state.value?.copy(
                event = UiEvent.PopBackStack
            )
        } else {
            _state.value = _state.value?.copy(
                event = UiEvent.ShowSnackbar(Constantes.ERROR_GUARDAR)
            )
        }
    }

    fun deleteCoche(coche: Coche) {
        if (!deleteCocheUseCase(coche)) {
            _state.value = _state.value?.copy(
                event = UiEvent.ShowSnackbar(Constantes.COCHE_ELIMINADO_EXITO)
            )
            _state.value = _state.value?.copy(
                event = UiEvent.PopBackStack
            )
        } else {
            _state.value = _state.value?.copy(
                event = UiEvent.ShowSnackbar(Constantes.ERROR_ELIMINAR)
            )
        }
    }

    fun getCoches(matricula: String) {
        val coches = getCoches()

        val coche = coches.find { it.matricula == matricula }

        if (coche == null) {
            _state.value = _state.value?.copy(
                event = UiEvent.ShowSnackbar(Constantes.ERROR_GET_COCHE)
            )
        } else {
            originalMatricula = coche.matricula
            _state.value = _state.value?.copy(coche = coche) ?: DetalleState(coche)
        }
    }

    fun limpiarMensaje() {
        _state.value = _state.value?.copy(event = null)
    }
}

class DetalleViewModelFactory(
    private val stringProvider: StringProvider,
    private val addCocheUseCase: AddCocheUseCase,
    private val updateCocheUseCase: UpdateCocheUseCase,
    private val deleteCocheUseCase: DeleteCocheUseCase,
    private val getCoches: GetCoches
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetalleViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DetalleViewModel(
                stringProvider,
                addCocheUseCase,
                updateCocheUseCase,
                deleteCocheUseCase,
                getCoches
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}