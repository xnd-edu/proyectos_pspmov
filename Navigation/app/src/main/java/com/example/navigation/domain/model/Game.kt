package com.example.navigation.domain.model

import com.google.gson.annotations.SerializedName
import java.util.Calendar

data class Game(
    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("rating")
    val rating: Double? = null,

    @SerializedName("summary")
    val summary: String? = null,

    @SerializedName("cover")
    val cover: GameCover? = null,

    @SerializedName("first_release_date")
    val firstReleaseDate: Long? = null
) {
    fun getCoverUrl(): String? {
        return cover?.imageId?.let { imageId ->
            "https://images.igdb.com/igdb/image/upload/t_cover_big/$imageId.webp"
        }
    }

    fun getFormattedRating(): String {
        return rating?.let {
            "${it.toInt()}/100"
        } ?: "N/A"
    }

    /**
     * Obtiene el año de lanzamiento.
     * Ejemplo: 1393977600 -> "2014"
     */
    fun getReleaseYear(): String? {
        return firstReleaseDate?.let { timestamp ->
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = timestamp * 1000L
            calendar[Calendar.YEAR].toString()
        }
    }
}

data class GameCover(
    @SerializedName("image_id")
    val imageId: String
)


