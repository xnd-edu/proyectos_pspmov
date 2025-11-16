package com.example.navigation.ui.conductores.main

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.navigation.R
import com.example.navigation.databinding.ItemConductorBinding
import com.example.navigation.domain.model.Conductor
import com.example.navigation.ui.conductores.main.ConductoresAdapter
import com.example.navigation.ui.common.StringProvider
import com.google.android.material.card.MaterialCardView
import com.google.android.material.listitem.ListItemLayout
import com.google.android.material.shape.ShapeAppearanceModel

class ConductorItemViewholder(
    itemView: View,
    val actions: ConductoresAdapter.ConductoresActions,
    private val stringProvider: StringProvider
) : RecyclerView.ViewHolder(itemView) {

    private val binding = ItemConductorBinding.bind(itemView)

    fun bind(item: Conductor, position: Int, itemCount: Int) {
        with(binding) {
            conductorNombre.text = stringProvider.getString(
                R.string.conductor_nombre,
                item.apellidos ?: "",
                item.nombre ?: ""
            )
            conductorDni.text = item.dni

            itemView.setOnLongClickListener {
                true
            }

            itemView.setOnClickListener {
                actions.onItemClick(item)
            }
        }

        // Actualizar la apariencia según la posición
        (itemView as? ListItemLayout)?.updateAppearance(position, itemCount)

        // Aplicar el shape appearance al MaterialCardView según la posición
        updateCardShape(position, itemCount)
    }

    private fun updateCardShape(position: Int, itemCount: Int) {
        val cardView = binding.root.getChildAt(0) as? MaterialCardView
        cardView?.let { card ->
            val context = card.context
            val cornerLarge = context.resources.getDimension(R.dimen.card_corner_radius_large)
            val cornerTiny = context.resources.getDimension(R.dimen.card_corner_radius_tiny)

            val shapeAppearance = when {
                itemCount == 1 -> {
                    ShapeAppearanceModel.builder()
                        .setAllCornerSizes(cornerLarge)
                        .build()
                }
                position == 0 -> {
                    ShapeAppearanceModel.builder()
                        .setTopLeftCornerSize(cornerLarge)
                        .setTopRightCornerSize(cornerLarge)
                        .setBottomLeftCornerSize(cornerTiny)
                        .setBottomRightCornerSize(cornerTiny)
                        .build()
                }
                position == itemCount - 1 -> {
                    ShapeAppearanceModel.builder()
                        .setTopLeftCornerSize(cornerTiny)
                        .setTopRightCornerSize(cornerTiny)
                        .setBottomLeftCornerSize(cornerLarge)
                        .setBottomRightCornerSize(cornerLarge)
                        .build()
                }
                else -> {
                    ShapeAppearanceModel.builder()
                        .setAllCornerSizes(cornerTiny)
                        .build()
                }
            }
            card.shapeAppearanceModel = shapeAppearance
        }
    }
}