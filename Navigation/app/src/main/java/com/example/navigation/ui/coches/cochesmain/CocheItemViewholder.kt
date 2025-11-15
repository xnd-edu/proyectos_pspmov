package com.example.navigation.ui.coches.cochesmain

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.navigation.R
import com.example.navigation.databinding.ItemCocheBinding
import com.example.navigation.domain.model.Coche
import com.example.navigation.ui.common.StringProvider
import com.google.android.material.listitem.ListItemLayout

class CocheItemViewholder(
    itemView: View,
    val actions: CochesAdapter.CochesActions,
    private val stringProvider: StringProvider
) : RecyclerView.ViewHolder(itemView) {

    private val binding = ItemCocheBinding.bind(itemView)

    fun bind(item: Coche, position: Int, itemCount: Int) {
        with(binding) {
            cocheNombre.text = stringProvider.getString(
                R.string.coche_nombre,
                item.marca ?: "",
                item.modelo ?: ""
            )
            cocheMatricula.text = item.matricula

            itemView.setOnLongClickListener {
                true
            }

            itemView.setOnClickListener {
                actions.onItemClick(item)
            }
        }

        // Actualizar la apariencia según la posición (esto ajusta las esquinas de las tarjetas)
        (itemView as? ListItemLayout)?.updateAppearance(position, itemCount)

        // Aplicar el shape appearance al MaterialCardView según la posición
        updateCardShape(position, itemCount)
    }

    private fun updateCardShape(position: Int, itemCount: Int) {
        val cardView = binding.root.getChildAt(0) as? com.google.android.material.card.MaterialCardView
        cardView?.let { card ->
            val context = card.context
            val cornerLarge = context.resources.getDimension(R.dimen.card_corner_radius_large)
            val cornerNone = 0f

            val shapeAppearance = when {
                itemCount == 1 -> {
                    // Item único: todas las esquinas redondeadas
                    com.google.android.material.shape.ShapeAppearanceModel.builder()
                        .setAllCornerSizes(cornerLarge)
                        .build()
                }
                position == 0 -> {
                    // Primer item: esquinas superiores redondeadas
                    com.google.android.material.shape.ShapeAppearanceModel.builder()
                        .setTopLeftCornerSize(cornerLarge)
                        .setTopRightCornerSize(cornerLarge)
                        .setBottomLeftCornerSize(cornerNone)
                        .setBottomRightCornerSize(cornerNone)
                        .build()
                }
                position == itemCount - 1 -> {
                    // Último item: esquinas inferiores redondeadas
                    com.google.android.material.shape.ShapeAppearanceModel.builder()
                        .setTopLeftCornerSize(cornerNone)
                        .setTopRightCornerSize(cornerNone)
                        .setBottomLeftCornerSize(cornerLarge)
                        .setBottomRightCornerSize(cornerLarge)
                        .build()
                }
                else -> {
                    // Items del medio: sin esquinas redondeadas
                    com.google.android.material.shape.ShapeAppearanceModel.builder()
                        .setAllCornerSizes(cornerNone)
                        .build()
                }
            }
            card.shapeAppearanceModel = shapeAppearance
        }
    }
}