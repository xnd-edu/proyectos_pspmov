package com.example.navigation.ui.coches.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.navigation.domain.usecases.coches.GetCoches
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CochesMainViewModel @Inject constructor(
    private val getCoches: GetCoches) : ViewModel() {

    private val _state = MutableLiveData(CocheMainState())
    val state: LiveData<CocheMainState> get() = _state

    init {
        loadCoches()
    }

    fun loadCoches() {
        viewModelScope.launch {
            try {
                val cochesList = getCoches()
                _state.value = _state.value?.copy(coches = cochesList)
            } catch (_: Exception) {
                // Error al cargar coches
            }
        }
    }
}