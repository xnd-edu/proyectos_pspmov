package com.example.composeapp.ui.screens.secrets.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composeapp.R
import com.example.composeapp.domain.usecases.secrets.AddSecretUseCase
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
class SecretAddViewModel @Inject constructor(
    private val stringProvider: StringProvider,
    private val addSecretUseCase: AddSecretUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(SecretAddState())
    val state: StateFlow<SecretAddState> = _state.asStateFlow()
    private val _events = Channel<UiEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    fun handleIntent(intent: SecretIntent) {
        when (intent) {
            is SecretIntent.ChangeSecretText -> _state.update { it.copy(secretText = intent.text) }
            is SecretIntent.ChangePassword -> _state.update { it.copy(password = intent.password) }
            is SecretIntent.SaveSecret -> saveSecret()
        }
    }

    private fun saveSecret() {
        viewModelScope.launch {
            val secretText = _state.value.secretText
            val password = _state.value.password

            // Validación básica
            if (secretText.isBlank()) {
                sendEvent(UiEvent.ShowSnackbar(stringProvider.getString(R.string.error_campos_vacios)))
                return@launch
            }

            if (password.isBlank()) {
                sendEvent(UiEvent.ShowSnackbar(stringProvider.getString(R.string.error_campos_vacios)))
                return@launch
            }

            addSecretUseCase(secretText, password).fold(
                onSuccess = {
                    sendEvent(UiEvent.ShowSnackbar(stringProvider.getString(R.string.secreto_guardado_exito, it)))
                    sendEvent(UiEvent.NavigateBack)
                },
                onFailure = {
                    sendEvent(UiEvent.ShowSnackbar(
                        stringProvider.getString(R.string.error_guardar_secreto)
                    ))
                }
            )
        }
    }

    private fun sendEvent(event: UiEvent) {
        viewModelScope.launch {
            _events.send(event)
        }
    }
}

