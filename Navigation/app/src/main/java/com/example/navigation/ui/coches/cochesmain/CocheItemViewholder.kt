package com.example.navigation.ui.coches.cochesmain

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.navigation.R
import com.example.navigation.databinding.ItemCocheBinding
import com.example.navigation.domain.model.Coche
import com.example.navigation.ui.common.StringProvider

class CocheItemViewholder(itemView: View, val actions:CochesAdapter.CochesActions, private val stringProvider: StringProvider) : RecyclerView.ViewHolder(itemView) {

    private val binding = ItemCocheBinding.bind(itemView)

    fun bind(item: Coche){
        with(binding) {
            cocheNombre.text = stringProvider.getString(
                R.string.coche_nombre,
                item.marca ?: "",
                item.modelo ?: ""
            )
            cocheMatricula.text = item.matricula

            itemView.setBackgroundResource(R.color.white)

            itemView.setOnLongClickListener{
                true
            }

            itemView.setOnClickListener {
               actions.onItemClick(item)
            }
        }
    }
}