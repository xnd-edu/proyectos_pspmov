package com.example.navigation.ui.coches.conductores

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.navigation.R
import com.example.navigation.databinding.ItemConductorEditBinding
import com.example.navigation.domain.model.Conductor
import com.example.navigation.ui.common.StringProvider
import com.google.android.material.card.MaterialCardView
import com.google.android.material.listitem.ListItemLayout
import com.google.android.material.shape.ShapeAppearanceModel

class CocheConductorItemViewholder(
    itemView: View,
    val actions: CochesConductoresAdapter.CochesConductoresActions,
    private val stringProvider: StringProvider
) : RecyclerView.ViewHolder(itemView) {

    private val binding = ItemConductorEditBinding.bind(itemView)

    fun bind(item: Conductor, position: Int, itemCount: Int) {
        with(binding) {
            conductorNombre.text = stringProvider.getString(
                R.string.conductor_nombre,
                item.apellidos ?: "",
                item.nombre ?: ""
            )
            conductorDni.text = item.dni


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