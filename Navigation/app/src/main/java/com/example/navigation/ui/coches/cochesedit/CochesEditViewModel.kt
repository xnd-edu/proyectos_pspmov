package com.example.navigation.ui.coches.cochesedit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.domain.usecases.coches.DeleteCocheUseCase
import com.example.navigation.R
import com.example.navigation.domain.model.Coche
import com.example.navigation.domain.usecases.coches.GetCoches
import com.example.navigation.domain.usecases.coches.UpdateCocheUseCase
import com.example.navigation.ui.common.StringProvider
import com.example.navigation.ui.common.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CochesEditViewModel @Inject constructor(
    private val stringProvider: StringProvider,
    private val updateCocheUseCase: UpdateCocheUseCase,
    private val deleteCocheUseCase: DeleteCocheUseCase,
    private val getCoches: GetCoches
) : ViewModel() {
    private var _state: MutableLiveData<CochesEditState> = MutableLiveData(CochesEditState(Coche()))
    val state: LiveData<CochesEditState> get() = _state
    private var originalMatricula: String? = null

    fun saveCoche(coche: Coche) {
        val success = updateCocheUseCase(originalMatricula!!, coche)

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

    fun deleteCoche(coche: Coche) {
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

    fun getCoche(matricula: String) {
        val coches = getCoches()

        val coche = coches.find { it.matricula == matricula }

        if (coche == null) {
            _state.value = _state.value?.copy(
                event = UiEvent.ShowSnackbar(stringProvider.getString(R.string.error_get_coche))
            )
        } else {
            originalMatricula = coche.matricula
            _state.value = _state.value?.copy(coche = coche) ?: CochesEditState(coche)
        }
    }

    fun limpiarMensaje() {
        _state.value = _state.value?.copy(event = null)
    }
}
