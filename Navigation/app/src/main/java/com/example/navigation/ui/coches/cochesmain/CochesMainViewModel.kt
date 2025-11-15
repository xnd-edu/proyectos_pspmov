package com.example.navigation.ui.coches.cochesmain

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.navigation.domain.usecases.coches.GetCoches
import dagger.hilt.android.lifecycle.HiltViewModel
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
        val cochesFromRepo = getCoches.invoke()
        _state.value = _state.value?.copy(coches = getCoches.invoke())
    }
}