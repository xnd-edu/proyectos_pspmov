package com.example.composeapp.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composeapp.R
import com.example.composeapp.data.common.NetworkError
import com.example.composeapp.data.common.NetworkErrorMapper
import com.example.composeapp.domain.usecases.auth.LoginUseCase
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
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val errorMapper: NetworkErrorMapper,
    private val stringProvider: StringProvider
) : ViewModel() {
    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state.asStateFlow()

    private val _events = Channel<UiEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    fun handleIntent(intent: LoginIntent) {
        when (intent) {
            is LoginIntent.OnUsernameChange -> {
                _state.update { it.copy(username = intent.username, error = null) }
            }
            is LoginIntent.OnPasswordChange -> {
                _state.update { it.copy(password = intent.password, error = null) }
            }
            LoginIntent.OnLoginClick -> login()
        }
    }

    private fun login() {
        val username = _state.value.username
        val password = _state.value.password

        if (username.isBlank() || password.isBlank()) {
            _state.update { it.copy(error = stringProvider.getString(R.string.login_campos_vacios)) }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            loginUseCase(username, password)
                .onSuccess {
                    _state.update { it.copy(isLoading = false, loginSuccess = true) }
                }
                .onFailure { error ->
                    val errorMessage = errorMapper.toMessage(error as NetworkError)

                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = errorMessage
                        )
                    }
                }
        }
    }
}

