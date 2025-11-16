package com.example.navigation.ui.conductores.coches

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.navigation.R
import com.example.navigation.databinding.ItemCocheEditBinding
import com.example.navigation.domain.model.Coche
import com.example.navigation.ui.common.StringProvider
import com.google.android.material.card.MaterialCardView
import com.google.android.material.listitem.ListItemLayout
import com.google.android.material.shape.ShapeAppearanceModel

class ConductorCocheItemViewholder(
    itemView: View,
    val actions: ConductoresCochesAdapter.ConductoresCochesActions,
    private val stringProvider: StringProvider
) : RecyclerView.ViewHolder(itemView) {

    private val binding = ItemCocheEditBinding.bind(itemView)

    fun bind(item: Coche, position: Int, itemCount: Int) {
        with(binding) {
            cocheNombre.text = stringProvider.getString(
                R.string.coche_nombre,
                item.marca ?: "",
                item.modelo ?: ""
            )
            cocheMatricula.text = item.matricula


            buttonBorrar.setOnClickListener {
                actions.onDeleteClick(item)
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

