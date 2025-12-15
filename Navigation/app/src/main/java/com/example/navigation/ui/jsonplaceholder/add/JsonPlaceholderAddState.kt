package com.example.navigation.ui.jsonplaceholder.add

import com.example.navigation.domain.model.JsonPlaceholderPost

data class JsonPlaceholderAddState(
    val conductor: JsonPlaceholderPost? = null,
    val isLoading: Boolean = false
)