package com.example.navigation.domain.model

import com.google.gson.annotations.SerializedName

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
)

data class GameCover(
    @SerializedName("image_id")
    val imageId: String
)


