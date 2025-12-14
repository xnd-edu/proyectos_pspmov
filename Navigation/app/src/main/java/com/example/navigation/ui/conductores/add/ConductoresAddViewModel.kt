package com.example.navigation.ui.conductores.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.navigation.R
import com.example.navigation.common.NetworkResult
import com.example.navigation.domain.model.Conductor
import com.example.navigation.domain.model.JsonPlaceholderPost
import com.example.navigation.domain.usecases.conductores.AddConductorUseCase
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
class ConductoresAddViewModel @Inject constructor(
    private val stringProvider: StringProvider,
    private val addConductorUseCase: AddConductorUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(ConductoresAddState())
    val state: StateFlow<ConductoresAddState> = _state.asStateFlow()

    fun handleIntent(intent: JsonPlaceholderAddIntent) {
        when (intent) {
            is JsonPlaceholderAddIntent.AddConductor -> addConductor(intent.jsonPlaceholderPost)
            is JsonPlaceholderAddIntent.LimpiarMensaje -> limpiarMensaje()
        }
    }

    private fun addConductor(jsonPlaceholderPost: JsonPlaceholderPost) {
        viewModelScope.launch {
            val result = addConductorUseCase(jsonPlaceholderPost)

            when (result) {
                is NetworkResult.Success -> {
                    _state.update { it.copy(event = UiEvent.ShowSnackbar(stringProvider.getString(R.string.conductor_guardado_exito))) }
                    _state.update { it.copy(event = UiEvent.PopBackStack) }
                }
                is NetworkResult.Loading -> {
                    // Optionally handle loading state
                }
                is NetworkResult.Error -> {
                    _state.update { it.copy(event = UiEvent.ShowSnackbar(stringProvider.getString(R.string.error_guardar))) }
                }
            }
        }
    }

    private fun limpiarMensaje() {
        _state.update { it.copy(event = null) }
    }
}
