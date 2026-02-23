package com.example.composeapp.ui.common

sealed class UiEvent(){
    data class ShowSnackbar(
        val message: String,
        val action: String? = null
    ): UiEvent()
    data object NavigateBack : UiEvent()
    data class NavigateToDetail(
        val id: Long,
        val password: String
    ) : UiEvent()
}
