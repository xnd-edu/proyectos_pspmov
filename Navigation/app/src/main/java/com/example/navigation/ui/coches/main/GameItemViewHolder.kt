package com.example.navigation.ui.coches.main

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.navigation.R
import com.example.navigation.databinding.ItemGameBinding
import com.example.navigation.domain.model.Game

class GameItemViewHolder(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {

    private val binding = ItemGameBinding.bind(itemView)

    fun bind(item: Game) {
        with(binding) {
            // Nombre del juego
            tvGameName.text = item.name

            // Año de lanzamiento
            tvGameReleaseDate.text = item.getReleaseYear() ?: "N/A"

            // Rating
            tvGameRating.text = item.getFormattedRating()

            // Resumen
            tvGameSummary.text = item.summary ?: "Sin descripción disponible"

            // Imagen del cover con Coil
            item.getCoverUrl()?.let { url ->
                ivCharacterImage.load(url) {
                    crossfade(true)
                    placeholder(R.drawable.ic_launcher_foreground)
                    error(R.drawable.ic_launcher_foreground)
                }
            } ?: run {
                // Si no hay cover, mostrar placeholder
                ivCharacterImage.setImageResource(R.drawable.ic_launcher_foreground)
            }
        }
    }
}

