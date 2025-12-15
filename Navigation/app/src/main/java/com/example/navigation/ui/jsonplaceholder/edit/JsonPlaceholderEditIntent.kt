package com.example.navigation.ui.jsonplaceholder.edit

import com.example.navigation.domain.model.JsonPlaceholderPost

interface JsonPlaceholderEditIntent {
    data class LoadPost(val id: Int) : JsonPlaceholderEditIntent
    data class UpdatePost(val jsonPlaceholderPost: JsonPlaceholderPost) : JsonPlaceholderEditIntent
    data class DeletePost(val jsonPlaceholderPost: JsonPlaceholderPost) : JsonPlaceholderEditIntent
}
