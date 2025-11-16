package com.example.navigation.ui.conductores.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.navigation.domain.usecases.conductores.GetConductores
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConductoresMainViewModel @Inject constructor(
    private val getConductores: GetConductores) : ViewModel() {

    private val _state = MutableLiveData(ConductoresMainState())
    val state: LiveData<ConductoresMainState> get() = _state

    init {
        loadConductores()
    }

    fun loadConductores() {
        viewModelScope.launch {
            try {
                val conductoresList = getConductores()
                _state.value = _state.value?.copy(conductores = conductoresList)
            } catch (_: Exception) {
                // Error al cargar conductores
            }
        }
    }
}