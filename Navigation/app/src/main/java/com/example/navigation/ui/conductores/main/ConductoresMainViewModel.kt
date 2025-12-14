package com.example.navigation.ui.conductores.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.navigation.common.NetworkResult
import com.example.navigation.domain.usecases.conductores.GetConductores
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConductoresMainViewModel @Inject constructor(
    private val getConductores: GetConductores
) : ViewModel() {

    private val _state = MutableStateFlow(ConductoresMainState())
    val state: StateFlow<ConductoresMainState> = _state.asStateFlow()

    init {
        loadConductores()
    }

    fun handleIntent(intent: JsonPlaceholderMainIntent) {
        when (intent) {
            is JsonPlaceholderMainIntent.LoadConductores -> loadConductores()
        }
    }

    private fun loadConductores() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            val result = getConductores()

            when (result) {
                is NetworkResult.Success -> {
                    _state.update {
                        it.copy(
                            conductores = result.data,
                            isLoading = false
                        )
                    }
                }

                is NetworkResult.Loading -> {
                    _state.update { it.copy(isLoading = true) }
                }

                is NetworkResult.Error -> {
                    _state.update { it.copy(isLoading = false) }
                }
            }
        }
    }
}
