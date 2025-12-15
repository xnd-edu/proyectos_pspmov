package com.example.navigation.ui.jsonplaceholder.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.navigation.R
import com.example.navigation.domain.model.JsonPlaceholderPost

class JsonPlaceholderAdapter(
    val actions: PostActions
) : ListAdapter<JsonPlaceholderPost, JsonPlaceholderItemViewholder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JsonPlaceholderItemViewholder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_post, parent, false)
        return JsonPlaceholderItemViewholder(view, actions)
    }

    override fun onBindViewHolder(holder: JsonPlaceholderItemViewholder, position: Int) {
        val item = getItem(position)
        holder.bind(item, position, itemCount)
    }

    class DiffCallback : DiffUtil.ItemCallback<JsonPlaceholderPost>() {
        override fun areItemsTheSame(oldItem: JsonPlaceholderPost, newItem: JsonPlaceholderPost): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: JsonPlaceholderPost, newItem: JsonPlaceholderPost): Boolean {
            return oldItem == newItem
        }
    }

    interface PostActions {
        fun onItemClick(jsonPlaceholderPost: JsonPlaceholderPost)

    }
}