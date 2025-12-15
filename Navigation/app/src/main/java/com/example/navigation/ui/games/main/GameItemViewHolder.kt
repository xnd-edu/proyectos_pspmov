package com.example.navigation.ui.games.main

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.navigation.R
import com.example.navigation.common.ApiConstants
import com.example.navigation.common.FormatConstants
import com.example.navigation.databinding.ItemGameBinding
import com.example.navigation.domain.model.Game
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class GameItemViewHolder(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {

    private val binding = ItemGameBinding.bind(itemView)
    private val context = itemView.context

    fun bind(item: Game) {
        with(binding) {
            tvGameName.text = item.name
            tvGameReleaseDate.text = formatReleaseDate(item.firstReleaseDate)
            tvGameRating.text = formatRating(item.rating)
            tvGameSummary.text = item.summary ?: context.getString(R.string.sin_descripcion_disponible)
            loadCoverImage(item)
        }
    }

    private fun formatReleaseDate(timestamp: Long?): String {
        return timestamp?.let {
            val date = Date(it * 1000L)
            val formatter = SimpleDateFormat(FormatConstants.DATE_FORMAT_DD_MM_YYYY, Locale.getDefault())
            formatter.format(date)
        } ?: context.getString(R.string.rating_not_available)
    }

    private fun formatRating(rating: Double?): String {
        return rating?.let {
            "${it.toInt()}${FormatConstants.RATING_FORMAT}"
        } ?: context.getString(R.string.rating_not_available)
    }

    private fun getCoverUrl(game: Game): String? {
        return game.cover?.imageId?.let { imageId ->
            "${ApiConstants.IGDB_IMAGE_BASE_URL}${ApiConstants.IGDB_COVER_SIZE}/$imageId.${ApiConstants.IGDB_IMAGE_FORMAT}"
        }
    }

    private fun loadCoverImage(game: Game) {
        getCoverUrl(game)?.let { url ->
            binding.ivCharacterImage.load(url) {
                crossfade(true)
                placeholder(R.drawable.ic_launcher_foreground)
                error(R.drawable.ic_launcher_foreground)
            }
        } ?: run {
            binding.ivCharacterImage.setImageResource(R.drawable.ic_launcher_foreground)
        }
    }
}

