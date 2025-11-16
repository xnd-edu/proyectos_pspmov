package com.example.navigation.ui.coches.conductores

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.navigation.R
import com.example.navigation.domain.model.Conductor
import com.example.navigation.ui.common.StringProvider

class CochesConductoresAdapter(
    val actions: CochesConductoresActions,
    private val stringProvider: StringProvider
) : ListAdapter<Conductor, CocheConductorItemViewholder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CocheConductorItemViewholder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_conductor_edit, parent, false)
        return CocheConductorItemViewholder(view, actions, stringProvider)
    }

    override fun onBindViewHolder(holder: CocheConductorItemViewholder, position: Int) {
        val item = getItem(position)
        holder.bind(item, position, itemCount)
    }

    class DiffCallback : DiffUtil.ItemCallback<Conductor>() {
        override fun areItemsTheSame(oldItem: Conductor, newItem: Conductor): Boolean {
            return oldItem.dni == newItem.dni
        }

        override fun areContentsTheSame(oldItem: Conductor, newItem: Conductor): Boolean {
            return oldItem == newItem
        }
    }

    interface CochesConductoresActions {
        fun onDeleteClick(conductor: Conductor)
    }
}