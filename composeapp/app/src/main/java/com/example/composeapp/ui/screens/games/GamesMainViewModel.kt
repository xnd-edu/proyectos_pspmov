package com.example.composeapp.ui.screens.games

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composeapp.data.common.NetworkError
import com.example.composeapp.data.common.NetworkErrorMapper
import com.example.composeapp.domain.usecases.games.SearchGamesUseCase
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
class GamesMainViewModel @Inject constructor(
    private val searchGamesUsecase: SearchGamesUseCase,
    private val errorMapper: NetworkErrorMapper
) : ViewModel() {
    private val _state = MutableStateFlow(GamesMainState())
    val state: StateFlow<GamesMainState> = _state.asStateFlow()
    private val _events = Channel<UiEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    fun handleIntent(intent: GamesMainIntent) {
        when (intent) {
            is GamesMainIntent.SearchGames -> searchGames(intent.query)
            is GamesMainIntent.OnSearchQueryChange -> onSearchQueryChange(intent.query)
        }
    }

    private fun onSearchQueryChange(query: String) {
        _state.update { it.copy(searchQuery = query) }
    }

    private fun searchGames(query: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            searchGamesUsecase(query)
                .onSuccess { games ->
                    _state.update { it.copy(games = games, isLoading = false) }
                }
                .onFailure { error ->
                    val errorMessage = errorMapper.toMessage(error as NetworkError)
                    _events.send(UiEvent.ShowSnackbar(errorMessage))
                    _state.update { it.copy(isLoading = false) }
                }
        }
    }
}