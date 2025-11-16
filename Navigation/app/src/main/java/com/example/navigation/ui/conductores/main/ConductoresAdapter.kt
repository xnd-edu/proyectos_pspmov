package com.example.navigation.ui.conductores.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.navigation.R
import com.example.navigation.domain.model.Conductor
import com.example.navigation.ui.common.StringProvider

class ConductoresAdapter(
    val actions: ConductoresActions,
    private val stringProvider: StringProvider
) : ListAdapter<Conductor, ConductorItemViewholder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConductorItemViewholder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_conductor, parent, false)
        return ConductorItemViewholder(view, actions, stringProvider)
    }

    override fun onBindViewHolder(holder: ConductorItemViewholder, position: Int) {
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

    interface ConductoresActions {
        fun onItemClick(conductor: Conductor)

    }
}