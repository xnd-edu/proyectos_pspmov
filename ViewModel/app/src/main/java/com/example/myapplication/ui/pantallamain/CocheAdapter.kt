package com.example.myapplication.ui.pantallamain

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.myapplication.domain.modelo.Coche
import com.example.myapplication.R

class CocheAdapter(
    val actions : CochesActions
) : ListAdapter<Coche, CocheItemViewholder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CocheItemViewholder {
        return CocheItemViewholder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.coche_view, parent, false),
            actions,
            )
    }

    override fun onBindViewHolder(holder: CocheItemViewholder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
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