package com.example.navigation.ui.jsonplaceholder.edit

import com.example.navigation.domain.model.JsonPlaceholderPost

data class JsonPlaceholderEditState(
    val conductor: JsonPlaceholderPost? = null,
    val isLoading: Boolean = false
)