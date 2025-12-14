package com.example.navigation.ui.conductores.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.navigation.R
import com.example.navigation.common.NetworkResult
import com.example.navigation.domain.model.JsonPlaceholderPost
import com.example.navigation.domain.usecases.conductores.DeleteConductorUseCase
import com.example.navigation.domain.usecases.conductores.GetConductorByDniUseCase
import com.example.navigation.domain.usecases.conductores.UpdateConductorUseCase
import com.example.navigation.ui.common.StringProvider
import com.example.navigation.ui.common.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConductoresEditViewModel @Inject constructor(
    private val stringProvider: StringProvider,
    private val updateConductorUseCase: UpdateConductorUseCase,
    private val deleteConductorUseCase: DeleteConductorUseCase,
    private val getConductor: GetConductorByDniUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(ConductoresEditState())
    val state: StateFlow<ConductoresEditState> = _state.asStateFlow()

    fun handleIntent(intent: JsonPlaceholderEditIntent) {
        when (intent) {
            is JsonPlaceholderEditIntent.LoadPost -> loadPost(intent.id)
            is JsonPlaceholderEditIntent.UpdatePost -> updatePost(intent.jsonPlaceholderPost)
            is JsonPlaceholderEditIntent.DeletePost -> deletePost(intent.jsonPlaceholderPost)
            is JsonPlaceholderEditIntent.LimpiarMensaje -> limpiarMensaje()
        }
    }

    private fun loadPost(id: Int) {
        viewModelScope.launch {
            val result = getConductor(id)

            when (result) {
                is NetworkResult.Success -> {
                    _state.update { it.copy(conductor = result.data) }
                }
                is NetworkResult.Loading -> {
                    // Optionally handle loading state
                }
                is NetworkResult.Error -> {
                    _state.update {
                        it.copy(event = UiEvent.ShowSnackbar(stringProvider.getString(R.string.error_get_conductor)))
                    }
                }
            }
        }
    }

    private fun updatePost(jsonPlaceholderPost: JsonPlaceholderPost) {
        viewModelScope.launch {
            val result = updateConductorUseCase(jsonPlaceholderPost)

            when (result) {
                is NetworkResult.Success -> {
                    _state.update {
                        it.copy(event = UiEvent.ShowSnackbar(stringProvider.getString(R.string.conductor_guardado_exito)))
                    }
                    _state.update { it.copy(event = UiEvent.PopBackStack) }
                }
                is NetworkResult.Loading -> {
                    // Optionally handle loading state
                }
                is NetworkResult.Error -> {
                    _state.update {
                        it.copy(event = UiEvent.ShowSnackbar(stringProvider.getString(R.string.error_guardar)))
                    }
                }
            }
        }
    }

    private fun deletePost(jsonPlaceholderPost: JsonPlaceholderPost) {
        viewModelScope.launch {
            val result = deleteConductorUseCase(jsonPlaceholderPost)

            when (result) {
                is NetworkResult.Success -> {
                    _state.update {
                        it.copy(event = UiEvent.ShowSnackbar(stringProvider.getString(R.string.conductor_eliminado_exito)))
                    }
                    _state.update { it.copy(event = UiEvent.PopBackStack) }
                }
                is NetworkResult.Loading -> {
                    // Optionally handle loading state
                }
                is NetworkResult.Error -> {
                    _state.update {
                        it.copy(event = UiEvent.ShowSnackbar(stringProvider.getString(R.string.error_eliminar)))
                    }
                }
            }
        }
    }

    private fun limpiarMensaje() {
        _state.update { it.copy(event = null) }
    }
}
