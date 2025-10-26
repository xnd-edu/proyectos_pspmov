package com.example.myapplication.ui.pantallamain

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.data.CocheRepository
import com.example.myapplication.domain.modelo.Coche
import com.example.myapplication.domain.usecases.coches.*

class MainViewModel : ViewModel() {
    private var _state : MutableLiveData<MainState> = MutableLiveData()
    val state : LiveData<MainState> get() = _state

    init {
        _state.value = MainState(sizeList = GetListSizeUseCase().invoke())
    }
    fun saveCoche(coche: Coche) {
        val useCase = AddCocheUseCase()
        if (useCase.invoke(coche))
            _state.value = _state.value?.copy(
                mensaje = "El coche ha sido guardado con exito",
                sizeList = GetListSizeUseCase().invoke()
            )
        else
            _state.value = _state.value?.copy(mensaje = "Ha ocurrido un error al guardar")
    }

    fun limpiarCoche() {
        _state.value = _state.value?.copy(coche = Coche())
    }

    fun deleteCoche() {
        val useCase = DeleteCocheUseCase()
        var indice = _state.value?.indiceCoche ?: 0
        val deleted = useCase.invoke(indice - 1)
        if (deleted) {
            var coche = Coche()
            val size = state.value?.sizeList ?: 0
            if (indice < size)
                coche = VerCocheUseCase().invoke(indice - 1)
            else indice = 0
            _state.value = _state.value?.copy(
                coche = coche,
                sizeList = GetListSizeUseCase().invoke(),
                indiceCoche = indice
            )
        } else _state.value = _state.value?.copy(mensaje = "Oops, something went wrong")
    }

    fun actualizarCoche(coche: Coche) {
        val useCase = UpdateCocheUseCase()
        val indice = _state.value?.indiceCoche ?: 0
        if (useCase.invoke(indice - 1, coche)) {
            _state.value = _state.value?.copy(
                coche = coche,
                mensaje = "El update ha sido un exito"
            )
        } else _state.value = _state.value?.copy(mensaje = "Ha habido un error")
    }

    fun irCocheAnterior() {
        val indice = _state.value?.indiceCoche ?: 0
        if (indice > 1) {
            val coche = VerCocheUseCase().invoke(indice - 2)
            _state.value = _state.value?.copy(
                coche = coche,
                indiceCoche = indice - 1
            )
        } else {
            _state.value = _state.value?.copy(mensaje = "No hay coches anteriores")
        }
    }

    fun irCocheSiguiente() {
        val indice = _state.value?.indiceCoche ?: 0
        val size = _state.value?.sizeList ?: 0
        if (indice != size) {
            val coche = VerCocheUseCase().invoke(indice)
            _state.value = _state.value?.copy(
                coche = coche,
                indiceCoche = indice + 1
            )
        } else {
            _state.value = _state.value?.copy(mensaje = "No hay más coches")
        }
    }

    fun limpiarMensaje() {
        _state.value = _state.value?.copy(mensaje = null)
    }

}

class MainViewModelFactory(): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}