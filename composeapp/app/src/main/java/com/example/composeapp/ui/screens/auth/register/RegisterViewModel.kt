package com.example.composeapp.ui.screens.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composeapp.R
import com.example.composeapp.data.common.NetworkError
import com.example.composeapp.data.common.NetworkErrorMapper
import com.example.composeapp.domain.usecases.auth.RegisterUseCase
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
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
    private val errorMapper: NetworkErrorMapper,
    private val stringProvider: StringProvider
) : ViewModel() {

    private val _state = MutableStateFlow(RegisterState())
    val state: StateFlow<RegisterState> = _state.asStateFlow()

    private val _events = Channel<UiEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    fun handleIntent(intent: RegisterIntent) {
        when (intent) {
            is RegisterIntent.OnUsernameChange -> _state.update { it.copy(username = intent.username, error = null) }
            is RegisterIntent.OnEmailChange -> _state.update { it.copy(email = intent.email, error = null) }
            is RegisterIntent.OnPasswordChange -> _state.update { it.copy(password = intent.password, error = null) }
            is RegisterIntent.OnNombreChange -> _state.update { it.copy(nombre = intent.nombre, error = null) }
            RegisterIntent.OnRegisterClick -> register()
        }
    }

    private fun register() {
        val state = _state.value

        if (state.username.isBlank() || state.email.isBlank() || state.password.isBlank() || state.nombre.isBlank()) {
            _state.update { it.copy(error = stringProvider.getString(R.string.register_campos_vacios)) }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            registerUseCase(
                username = state.username,
                email = state.email,
                password = state.password,
                nombre = state.nombre
            )
                .onSuccess {
                    _state.update { it.copy(isLoading = false, registerSuccess = true) }
                }
                .onFailure { error ->
                    val errorMessage = errorMapper.toMessage(error as NetworkError)
                    _state.update { it.copy(isLoading = false, error = errorMessage) }
                }
        }
    }
}

