package com.example.navigation.ui.conductores.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.navigation.R
import com.example.navigation.domain.model.JsonPlaceholderPost
import com.example.navigation.ui.common.StringProvider

class ConductoresAdapter(
    val actions: ConductoresActions
) : ListAdapter<JsonPlaceholderPost, ConductorItemViewholder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConductorItemViewholder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_conductor, parent, false)
        return ConductorItemViewholder(view, actions)
    }

    override fun onBindViewHolder(holder: ConductorItemViewholder, position: Int) {
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

    interface ConductoresActions {
        fun onItemClick(jsonPlaceholderPost: JsonPlaceholderPost)

    }
}