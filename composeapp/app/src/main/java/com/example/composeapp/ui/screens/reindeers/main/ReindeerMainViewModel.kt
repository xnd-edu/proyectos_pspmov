package com.example.composeapp.ui.screens.reindeers.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composeapp.data.common.NetworkError
import com.example.composeapp.data.common.NetworkErrorMapper
import com.example.composeapp.domain.usecases.auth.IsUserAdminUseCase
import com.example.composeapp.domain.usecases.reindeer.user.GetReindeersUseCase
import com.example.composeapp.domain.usecases.reindeer.admin.GetReindeersAdminUseCase
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
class ReindeerMainViewModel @Inject constructor(
    private val errorMapper: NetworkErrorMapper,
    private val getReindeers: GetReindeersUseCase,
    private val getReindeersAdmin: GetReindeersAdminUseCase,
    private val isUserAdmin: IsUserAdminUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(ReindeerMainState(isAdmin = isUserAdmin()))
    val state: StateFlow<ReindeerMainState> = _state.asStateFlow()
    private val _events = Channel<UiEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    init {
        loadReindeers()
    }

    fun handleIntent(intent: ReindeerMainIntent) {
        when (intent) {
            is ReindeerMainIntent.LoadReindeers -> loadReindeers()
            is ReindeerMainIntent.OnSearchTextChange -> onSearchTextChange(intent.text)
        }
    }

    private fun loadReindeers() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            if (isUserAdmin()) {
                getReindeersAdmin()
                    .onSuccess { reindeers ->
                        _state.update {
                            it.copy(
                                reindeers = reindeers,
                                isLoading = false,
                                error = null
                            )
                        }
                    }
                    .onFailure { error ->
                        val errorMessage = if (error is NetworkError) {
                            errorMapper.toMessage(error)
                        } else {
                            error.message ?: "Error desconocido"
                        }

                        _state.update {
                            it.copy(
                                isLoading = false,
                                error = errorMessage
                            )
                        }
                    }
            } else {
                getReindeers()
                    .onSuccess { reindeers ->
                        _state.update {
                            it.copy(
                                reindeers = reindeers,
                                isLoading = false,
                                error = null
                            )
                        }
                    }
                    .onFailure { error ->
                        val errorMessage = if (error is NetworkError) {
                            errorMapper.toMessage(error)
                        } else {
                            error.message ?: "Error desconocido"
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
    }

    private fun onSearchTextChange(text: String) {
        _state.update { it.copy(searchText = text) }
    }
}

