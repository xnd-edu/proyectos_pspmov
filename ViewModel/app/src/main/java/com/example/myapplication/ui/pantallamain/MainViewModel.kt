package com.example.myapplication.ui.pantallamain

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.domain.usecases.coches.GetCoches

class MainViewModel(
    private val getCoches: GetCoches,
) : ViewModel() {
    private val _state = MutableLiveData(MainState())
    val state: LiveData<MainState> get() = _state

    init {
        getCoches()
    }

    fun getCoches() {
        _state.value = _state.value?.copy(coches = getCoches.invoke())
    }
}

class MainViewModelFactory(
    private val getCoches: GetCoches,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(
                getCoches,
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}