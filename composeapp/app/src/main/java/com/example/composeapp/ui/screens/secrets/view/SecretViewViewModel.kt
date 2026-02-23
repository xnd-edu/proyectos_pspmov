package com.example.composeapp.ui.screens.secrets.view

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.composeapp.R
import com.example.composeapp.domain.usecases.secrets.DeleteSecretUseCase
import com.example.composeapp.domain.usecases.secrets.GetSecretByIdUseCase
import com.example.composeapp.domain.usecases.sharesecrets.RevokeSharedSecretUseCase
import com.example.composeapp.domain.usecases.sharesecrets.ShareSecretUseCase
import com.example.composeapp.ui.common.StringProvider
import com.example.composeapp.ui.common.UiEvent
import com.example.composeapp.ui.navigation.routes.ViewSecret
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
class SecretViewViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val stringProvider: StringProvider,
    private val getSecretById: GetSecretByIdUseCase,
    private val deleteSecret: DeleteSecretUseCase,
    private val shareSecretUseCase: ShareSecretUseCase,
    private val revokeSharedSecretUseCase: RevokeSharedSecretUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(SecretViewState())
    val state: StateFlow<SecretViewState> = _state.asStateFlow()
    private val _events = Channel<UiEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()
    private val viewSecret: ViewSecret = savedStateHandle.toRoute()

    init {
        loadSecret(viewSecret.id, viewSecret.password)
    }

    fun handleIntent(intent: SecretViewIntent) {
        when (intent) {
            is SecretViewIntent.DeleteSecret -> deleteSecretAction()
            is SecretViewIntent.ShareSecret -> {
                _state.update { it.copy(isShareDialogVisible = true, usernameInput = "") }
            }
            is SecretViewIntent.RevokeSecret -> revokeSharedSecretAction()
            is SecretViewIntent.OnUsernameChange -> {
                _state.update { it.copy(usernameInput = intent.username) }
            }
            is SecretViewIntent.OnDismissDialog -> {
                _state.update { it.copy(isShareDialogVisible = false, usernameInput = "") }
            }
            is SecretViewIntent.OnConfirmShare -> shareSecretAction()
        }
    }

    private fun loadSecret(id: Long, password: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            getSecretById(id, password).fold(
                onSuccess = { secret ->
                    _state.update { it.copy(secret = secret, isLoading = false) }
                },
                onFailure = {
                    _state.update { it.copy(isLoading = false) }
                    sendEvent(
                        UiEvent.ShowSnackbar(
                            stringProvider.getString(R.string.error_cargar_secreto)
                        )
                    )
                    sendEvent(
                        UiEvent.NavigateBack
                    )
                }
            )
        }
    }

    private fun deleteSecretAction() {
        viewModelScope.launch {
            val secret = _state.value.secret ?: return@launch
            val secretId = secret.id

            deleteSecret(secretId).fold(
                onSuccess = {
                    sendEvent(UiEvent.ShowSnackbar(stringProvider.getString(R.string.secreto_eliminado_exito)))
                    sendEvent(UiEvent.NavigateBack)
                },
                onFailure = {
                    sendEvent(
                        UiEvent.ShowSnackbar(
                            stringProvider.getString(R.string.error_eliminar_secreto)
                        )
                    )
                }
            )
        }
    }

    private fun shareSecretAction() {
        val username = state.value.usernameInput
        val secret = state.value.secret

        if (username.isBlank() || secret?.id == null) {
            sendEvent(UiEvent.ShowSnackbar(stringProvider.getString(R.string.error_campos_vacios)))
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, isShareDialogVisible = false) }

            shareSecretUseCase(secret.id, username, secret.decryptedData).fold(
                onSuccess = {
                    sendEvent(UiEvent.ShowSnackbar(stringProvider.getString(R.string.secreto_compartido_exito, username)))
                    _state.update { it.copy(isLoading = false, usernameInput = "") }
                },
                onFailure = {
                    sendEvent(UiEvent.ShowSnackbar(stringProvider.getString(R.string.error_compartir_secreto)))
                    _state.update { it.copy(isLoading = false) }
                }
            )
        }
    }

    private fun revokeSharedSecretAction() {
        viewModelScope.launch {
            val secret = _state.value.secret ?: return@launch
            val secretId = secret.id

            revokeSharedSecretUseCase(secretId).fold(
                onSuccess = {
                    sendEvent(UiEvent.ShowSnackbar(stringProvider.getString(R.string.secreto_revocado_exito)))
                    sendEvent(UiEvent.NavigateBack)
                },
                onFailure = {
                    sendEvent(
                        UiEvent.ShowSnackbar(
                            stringProvider.getString(R.string.error_revocar_secreto)
                        )
                    )
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


