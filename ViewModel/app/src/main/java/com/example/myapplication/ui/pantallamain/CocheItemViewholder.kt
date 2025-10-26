package com.example.myapplication.ui.pantallamain

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.CocheViewBinding
import com.example.myapplication.domain.modelo.Coche

class CocheItemViewholder(itemView: View, val actions:CocheAdapter.CochesActions) : RecyclerView.ViewHolder(itemView) {

    private val binding = CocheViewBinding.bind(itemView)

    fun bind(item: Coche){
        with(binding) {
            textViewMatricula.text = item.matricula
            textViewMarca.text = item.marca
            textViewModelo.text = item.modelo

            itemView.setBackgroundResource(android.R.color.white)

            itemView.setOnLongClickListener{
                true
            }

            itemView.setOnClickListener {
               actions.onItemClick(item)
            }
        }
    }
}