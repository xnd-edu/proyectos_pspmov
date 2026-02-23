package com.example.composeapp.ui.screens.secrets.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composeapp.R
import com.example.composeapp.data.common.NetworkError
import com.example.composeapp.data.common.NetworkErrorMapper
import com.example.composeapp.domain.usecases.secrets.GetSecretsUseCase
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
class SecretMainViewModel @Inject constructor(
    private val errorMapper: NetworkErrorMapper,
    private val getSecrets: GetSecretsUseCase,
    private val stringProvider: StringProvider
) : ViewModel() {
    private val _state = MutableStateFlow(SecretMainState())
    val state: StateFlow<SecretMainState> = _state.asStateFlow()
    private val _events = Channel<UiEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    init {
        loadSecrets()
    }

    fun handleIntent(intent: SecretMainIntent) {
        when (intent) {
            is SecretMainIntent.LoadSecrets -> loadSecrets()
            is SecretMainIntent.OnSecretClick -> {
                _state.update {
                    it.copy(selectedSecretId = intent.id, passwordInput = "")
                }
            }
            is SecretMainIntent.OnPasswordChange -> {
                _state.update { it.copy(passwordInput = intent.password) }
            }
            is SecretMainIntent.OnDismissDialog -> {
                _state.update { it.copy(selectedSecretId = null, passwordInput = "") }
            }
            is SecretMainIntent.OnConfirmPassword -> confirmPassword()
        }
    }

    private fun loadSecrets() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            getSecrets()
                .onSuccess { secrets ->
                    _state.update {
                        it.copy(
                            secrets = secrets,
                            isLoading = false,
                            error = null
                        )
                    }
                }
                .onFailure { error ->
                    val errorMessage = if (error is NetworkError) {
                        errorMapper.toMessage(error)
                    } else {
                        error.message ?: stringProvider.getString(R.string.error_cargar_secretos)
                    }

                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = errorMessage
                        )
                    }

                }
        }
    }

    private fun confirmPassword() {
        viewModelScope.launch {
            val currentId = state.value.selectedSecretId
            val currentPassword = state.value.passwordInput

            if (currentId != null && currentPassword.isNotBlank()) {
                viewModelScope.launch {
                    // Enviamos el evento de navegación
                    _events.send(UiEvent.NavigateToDetail(currentId, currentPassword))
                    // Cerramos el diálogo
                    _state.update { it.copy(selectedSecretId = null, passwordInput = "") }
                }
            } else {
                viewModelScope.launch {
                    _events.send(UiEvent.ShowSnackbar(stringProvider.getString(R.string.error_campos_vacios)))
                }
            }
        }
    }
}

