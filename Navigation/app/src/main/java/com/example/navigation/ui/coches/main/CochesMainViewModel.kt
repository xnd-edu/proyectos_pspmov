package com.example.navigation.ui.coches.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.navigation.R
import com.example.navigation.common.NetworkResult
import com.example.navigation.domain.usecases.coches.GetCoches
import com.example.navigation.domain.usecases.igdb.SearchGamesUsecase
import com.example.navigation.ui.common.StringProvider
import com.example.navigation.ui.common.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CochesMainViewModel @Inject constructor(
    private val searchGamesUsecase: SearchGamesUsecase,
    private val stringProvider: StringProvider
) : ViewModel() {
    private val _state = MutableStateFlow(CocheMainState())
    val state: StateFlow<CocheMainState> = _state.asStateFlow()

    fun handleIntent(intent: GameMainIntent) {
        when (intent) {
            is GameMainIntent.SearchGames -> searchGames(intent.query)
        }
    }

    private fun searchGames(query: String) {
        viewModelScope.launch {
            val gamesList = searchGamesUsecase(query)
            _state.update { it.copy(isLoading = true) }

            when (gamesList) {
                is NetworkResult.Success -> {
                    _state.update { it.copy(coches = gamesList.data) }
                    _state.update { it.copy(isLoading = false) }
                }
                is NetworkResult.Loading -> {
                    _state.update { it.copy(isLoading = true) }
                }
                is NetworkResult.Error -> {
                    _state.update { it.copy(event = UiEvent.ShowSnackbar(stringProvider.getString(R.string.error_guardar))) }
                    _state.update { it.copy(isLoading = false) }
                }
            }
        }
    }
}