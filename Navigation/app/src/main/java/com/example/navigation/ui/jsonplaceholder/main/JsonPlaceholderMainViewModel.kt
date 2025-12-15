package com.example.navigation.ui.jsonplaceholder.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.navigation.common.NetworkResult
import com.example.navigation.common.toMessage
import com.example.navigation.domain.usecases.jsonplaceholder.GetPosts
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
class JsonPlaceholderMainViewModel @Inject constructor(
    private val getPosts: GetPosts,
    private val stringProvider: StringProvider
) : ViewModel() {

    private val _state = MutableStateFlow(JsonPlaceholderMainState())
    val state: StateFlow<JsonPlaceholderMainState> = _state.asStateFlow()
    private val _events = Channel<UiEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    init {
        loadPosts()
    }

    fun handleIntent(intent: JsonPlaceholderMainIntent) {
        when (intent) {
            is JsonPlaceholderMainIntent.LoadPosts -> loadPosts()
        }
    }

    private fun loadPosts() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            val result = getPosts()

            when (result) {
                is NetworkResult.Success -> {
                    _state.update {
                        it.copy(
                            posts = result.data,
                            isLoading = false
                        )
                    }
                }

                is NetworkResult.Loading -> {
                    _state.update { it.copy(isLoading = true) }
                }

                is NetworkResult.Error -> {
                    _events.send(UiEvent.ShowSnackbar(result.error.toMessage(stringProvider)))
                    _state.update { it.copy(isLoading = false) }
                }
            }
        }
    }
}
