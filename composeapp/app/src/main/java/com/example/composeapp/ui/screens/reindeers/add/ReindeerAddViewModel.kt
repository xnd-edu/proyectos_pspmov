package com.example.composeapp.ui.screens.reindeers.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composeapp.R
import com.example.composeapp.domain.model.Reindeer
import com.example.composeapp.domain.usecases.reindeer.admin.AddReindeerUseCase
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
class ReindeerAddViewModel @Inject constructor(
    private val stringProvider: StringProvider,
    private val addReindeerUseCase: AddReindeerUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(ReindeerAddState())
    val state: StateFlow<ReindeerAddState> = _state.asStateFlow()
    private val _events = Channel<UiEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    fun handleIntent(intent: ReindeerIntent) {
        when (intent) {
            is ReindeerIntent.ChangeReindeer -> updateReindeerState(intent.reindeer)
            is ReindeerIntent.SaveReindeer -> saveReindeer()
        }
    }

    private fun saveReindeer() {
        viewModelScope.launch {
            val reindeer = _state.value.reindeer

            // Validación básica
            if (reindeer.nombre.isNullOrBlank()) {
                sendEvent(UiEvent.ShowSnackbar(stringProvider.getString(R.string.error_nombre_vacio)))
                return@launch
            }

            if (reindeer.userId == null) {
                sendEvent(UiEvent.ShowSnackbar(stringProvider.getString(R.string.error_userid_vacio)))
                return@launch
            }

            addReindeerUseCase(reindeer).fold(
                onSuccess = {
                    sendEvent(UiEvent.ShowSnackbar(stringProvider.getString(R.string.reno_añadido_exito)))
                    sendEvent(UiEvent.NavigateBack)
                },
                onFailure = {
                    sendEvent(UiEvent.ShowSnackbar(
                        stringProvider.getString(R.string.error_añadir_reno)
                    ))
                }
            )
        }
    }

    private fun updateReindeerState(reindeer: Reindeer) {
        _state.update { it.copy(reindeer = reindeer) }
    }

    private fun sendEvent(event: UiEvent) {
        viewModelScope.launch {
            _events.send(event)
        }
    }
}

