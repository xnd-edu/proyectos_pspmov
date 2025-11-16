package com.example.navigation.ui.conductores.coches

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.navigation.R
import com.example.navigation.domain.model.Coche
import com.example.navigation.ui.common.StringProvider

class ConductoresCochesAdapter(
    val actions: ConductoresCochesActions,
    private val stringProvider: StringProvider
) : ListAdapter<Coche, ConductorCocheItemViewholder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConductorCocheItemViewholder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_coche_edit, parent, false)
        return ConductorCocheItemViewholder(view, actions, stringProvider)
    }

    override fun onBindViewHolder(holder: ConductorCocheItemViewholder, position: Int) {
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

    interface ConductoresCochesActions {
        fun onDeleteClick(coche: Coche)
    }
}

