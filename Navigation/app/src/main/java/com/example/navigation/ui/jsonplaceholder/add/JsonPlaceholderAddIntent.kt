package com.example.navigation.ui.jsonplaceholder.add

import com.example.navigation.domain.model.JsonPlaceholderPost

interface JsonPlaceholderAddIntent {
    data class AddConductor(val jsonPlaceholderPost: JsonPlaceholderPost): JsonPlaceholderAddIntent
}