package com.example.navigation.ui.jsonplaceholder.main

import com.example.navigation.domain.model.JsonPlaceholderPost

data class JsonPlaceholderMainState(
    val posts: List<JsonPlaceholderPost> = emptyList(),
    val isLoading: Boolean = false
)