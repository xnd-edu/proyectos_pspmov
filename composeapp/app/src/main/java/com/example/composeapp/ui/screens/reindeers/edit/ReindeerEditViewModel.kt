package com.example.composeapp.ui.screens.reindeers.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.composeapp.R
import com.example.composeapp.domain.model.Reindeer
import com.example.composeapp.domain.usecases.auth.IsUserAdminUseCase
import com.example.composeapp.domain.usecases.reindeer.admin.DeleteReindeerAdminUseCase
import com.example.composeapp.domain.usecases.reindeer.admin.GetReindeerByIdAdminUseCase
import com.example.composeapp.domain.usecases.reindeer.admin.UpdateReindeerUseCase
import com.example.composeapp.domain.usecases.reindeer.user.DeleteReindeerUseCase
import com.example.composeapp.domain.usecases.reindeer.user.GetReindeerByIdUseCase
import com.example.composeapp.ui.common.StringProvider
import com.example.composeapp.ui.common.UiEvent
import com.example.composeapp.ui.navigation.routes.EditReindeer
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
class ReindeerEditViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val stringProvider: StringProvider,
    private val getReindeerById: GetReindeerByIdUseCase,
    private val getReindeerByIdAdmin: GetReindeerByIdAdminUseCase,
    private val updateReindeer: UpdateReindeerUseCase,
    private val deleteReindeer: DeleteReindeerUseCase,
    private val deleteReindeerAdmin: DeleteReindeerAdminUseCase,
    private val isUserAdmin: IsUserAdminUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(ReindeerEditState(isAdmin = isUserAdmin()))
    val state: StateFlow<ReindeerEditState> = _state.asStateFlow()
    private val _events = Channel<UiEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    init {
        val editReindeer: EditReindeer = savedStateHandle.toRoute()
        loadReindeer(editReindeer.id)
    }

    fun handleIntent(intent: ReindeerEditIntent) {
        when (intent) {
            is ReindeerEditIntent.ChangeReindeer -> updateReindeerState(intent.reindeer)
            is ReindeerEditIntent.SaveReindeer -> saveReindeer()
            is ReindeerEditIntent.DeleteReindeer -> deleteReindeerAction()
        }
    }

    private fun loadReindeer(id: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            if (isUserAdmin()) {
                getReindeerByIdAdmin(id).fold(
                    onSuccess = { reindeer ->
                        _state.update { it.copy(reindeer = reindeer, isLoading = false) }
                    },
                    onFailure = {
                        _state.update { it.copy(isLoading = false) }
                        sendEvent(UiEvent.ShowSnackbar(
                            stringProvider.getString(R.string.error_cargar_reno)
                        ))
                    }
                )
            } else {
                getReindeerById(id).fold(
                    onSuccess = { reindeer ->
                        _state.update { it.copy(reindeer = reindeer, isLoading = false) }
                    },
                    onFailure = {
                        _state.update { it.copy(isLoading = false) }
                        sendEvent(UiEvent.ShowSnackbar(
                            stringProvider.getString(R.string.error_cargar_reno)
                        ))
                    }
                )
            }
        }
    }

    private fun saveReindeer() {
        viewModelScope.launch {
            val reindeer = _state.value.reindeer ?: return@launch

            // Validación básica
            if (reindeer.nombre.isNullOrBlank()) {
                sendEvent(UiEvent.ShowSnackbar(stringProvider.getString(R.string.error_nombre_vacio)))
                return@launch
            }

            if (reindeer.userId == null) {
                sendEvent(UiEvent.ShowSnackbar(stringProvider.getString(R.string.error_userid_vacio)))
                return@launch
            }

            updateReindeer(reindeer).fold(
                onSuccess = {
                    sendEvent(UiEvent.ShowSnackbar(stringProvider.getString(R.string.reno_actualizado_exito)))
                    sendEvent(UiEvent.NavigateBack)
                },
                onFailure = {
                    sendEvent(UiEvent.ShowSnackbar(
                        stringProvider.getString(R.string.error_actualizar_reno)
                    ))
                }
            )
        }
    }

    private fun deleteReindeerAction() {
        viewModelScope.launch {
            val reindeer = _state.value.reindeer ?: return@launch
            val reindeerId = reindeer.id ?: return@launch

            if (isUserAdmin()) {
                deleteReindeerAdmin(reindeerId).fold(
                    onSuccess = {
                        sendEvent(UiEvent.ShowSnackbar(stringProvider.getString(R.string.reno_eliminado_exito)))
                        sendEvent(UiEvent.NavigateBack)
                    },
                    onFailure = {
                        sendEvent(UiEvent.ShowSnackbar(
                            stringProvider.getString(R.string.error_eliminar_reno)
                        ))
                    }
                )
            } else {
                deleteReindeer(reindeerId).fold(
                    onSuccess = {
                        sendEvent(UiEvent.ShowSnackbar(stringProvider.getString(R.string.reno_eliminado_exito)))
                        sendEvent(UiEvent.NavigateBack)
                    },
                    onFailure = {
                        sendEvent(
                            UiEvent.ShowSnackbar(
                                stringProvider.getString(R.string.error_eliminar_reno)
                            )
                        )
                    }
                )
            }
        }
    }

    private fun updateReindeerState(reindeer: Reindeer) {
        _state.update { it.copy(reindeer = reindeer) }
    }

    private fun sendEvent(event: UiEvent) {
        viewModelScope.launch {
            _events.send(event)
        }
    }
}

