package com.example.navigation.ui.games.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.navigation.common.NetworkResult
import com.example.navigation.common.toMessage
import com.example.navigation.domain.usecases.games.SearchGamesUseCase
import com.example.navigation.ui.common.StringProvider
import com.example.navigation.ui.common.UiEvent
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
    private val stringProvider: StringProvider
) : ViewModel() {
    private val _state = MutableStateFlow(GamesMainState())
    val state: StateFlow<GamesMainState> = _state.asStateFlow()
    private val _events = Channel<UiEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    fun handleIntent(intent: GamesMainIntent) {
        when (intent) {
            is GamesMainIntent.SearchGames -> searchGames(intent.query)
        }
    }

    private fun searchGames(query: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val gamesList = searchGamesUsecase(query)

            when (gamesList) {
                is NetworkResult.Success -> {
                    _state.update { it.copy(games = gamesList.data, isLoading = false) }
                }
                is NetworkResult.Loading -> {
                    _state.update { it.copy(isLoading = true) }
                }
                is NetworkResult.Error -> {
                    _events.send(UiEvent.ShowSnackbar(gamesList.error.toMessage(stringProvider)))
                    _state.update { it.copy(isLoading = false) }
                }
            }
        }
    }
}