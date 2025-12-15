package com.example.navigation.ui.jsonplaceholder.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.navigation.R
import com.example.navigation.common.NetworkResult
import com.example.navigation.domain.model.JsonPlaceholderPost
import com.example.navigation.domain.usecases.jsonplaceholder.DeletePostUseCase
import com.example.navigation.domain.usecases.jsonplaceholder.GetPostByIdUseCase
import com.example.navigation.domain.usecases.jsonplaceholder.UpdatePostUseCase
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
class JsonPlaceholderEditViewModel @Inject constructor(
    private val stringProvider: StringProvider,
    private val updatePostUseCase: UpdatePostUseCase,
    private val deletePostUseCase: DeletePostUseCase,
    private val getConductor: GetPostByIdUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(JsonPlaceholderEditState())
    val state: StateFlow<JsonPlaceholderEditState> = _state.asStateFlow()
    private val _events = Channel<UiEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    fun handleIntent(intent: JsonPlaceholderEditIntent) {
        when (intent) {
            is JsonPlaceholderEditIntent.LoadPost -> loadPost(intent.id)
            is JsonPlaceholderEditIntent.UpdatePost -> updatePost(intent.jsonPlaceholderPost)
            is JsonPlaceholderEditIntent.DeletePost -> deletePost(intent.jsonPlaceholderPost)
        }
    }

    private fun loadPost(id: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val result = getConductor(id)

            when (result) {
                is NetworkResult.Success -> {
                    _state.update { it.copy(conductor = result.data, isLoading = false) }
                }
                is NetworkResult.Loading -> {
                    _state.update { it.copy(isLoading = true) }
                }
                is NetworkResult.Error -> {
                    _state.update { it.copy(isLoading = false) }
                    _events.send(UiEvent.ShowSnackbar(stringProvider.getString(R.string.error_get_post)))
                }
            }
        }
    }

    private fun updatePost(jsonPlaceholderPost: JsonPlaceholderPost) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val result = updatePostUseCase(jsonPlaceholderPost)

            when (result) {
                is NetworkResult.Success -> {
                    _state.update { it.copy(isLoading = false) }
                    _events.send(UiEvent.ShowSnackbar(stringProvider.getString(R.string.post_guardado_exito)))
                    _events.send(UiEvent.PopBackStack)
                }
                is NetworkResult.Loading -> {
                    _state.update { it.copy(isLoading = true) }
                }
                is NetworkResult.Error -> {
                    _state.update { it.copy(isLoading = false) }
                    _events.send(UiEvent.ShowSnackbar(stringProvider.getString(R.string.error_guardar)))
                }
            }
        }
    }

    private fun deletePost(jsonPlaceholderPost: JsonPlaceholderPost) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val result = deletePostUseCase(jsonPlaceholderPost)

            when (result) {
                is NetworkResult.Success -> {
                    _state.update { it.copy(isLoading = false) }
                    _events.send(UiEvent.ShowSnackbar(stringProvider.getString(R.string.post_eliminado_exito)))
                    _events.send(UiEvent.PopBackStack)
                }
                is NetworkResult.Loading -> {
                    _state.update { it.copy(isLoading = true) }
                }
                is NetworkResult.Error -> {
                    _state.update { it.copy(isLoading = false) }
                    _events.send(UiEvent.ShowSnackbar(stringProvider.getString(R.string.error_eliminar)))
                }
            }
        }
    }
}
