package com.example.navigation.ui.coches.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.navigation.R
import com.example.navigation.domain.model.Coche
import com.example.navigation.ui.common.StringProvider

class CochesAdapter(
    val actions: CochesActions,
    private val stringProvider: StringProvider
) : ListAdapter<Coche, CocheItemViewholder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CocheItemViewholder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_coche, parent, false)
        return CocheItemViewholder(view, actions, stringProvider)
    }

    override fun onBindViewHolder(holder: CocheItemViewholder, position: Int) {
        val item = getItem(position)
        holder.bind(item, position, itemCount)
    }

    class DiffCallback : DiffUtil.ItemCallback<Coche>() {
        override fun areItemsTheSame(oldItem: Coche, newItem: Coche): Boolean {
            return oldItem.matricula == newItem.matricula
        }

        override fun areContentsTheSame(oldItem: Coche, newItem: Coche): Boolean {
            return oldItem == newItem
        }
    }

    interface CochesActions {
        fun onItemClick(coche: Coche)

    }
}