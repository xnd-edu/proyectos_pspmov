package com.example.navigation.domain.model

import com.example.navigation.common.IgdbFieldNames
import com.google.gson.annotations.SerializedName

data class Game(
    @SerializedName(IgdbFieldNames.ID)
    val id: Int,

    @SerializedName(IgdbFieldNames.NAME)
    val name: String,

    @SerializedName(IgdbFieldNames.RATING)
    val rating: Double? = null,

    @SerializedName(IgdbFieldNames.SUMMARY)
    val summary: String? = null,

    @SerializedName(IgdbFieldNames.COVER)
    val cover: GameCover? = null,

    @SerializedName(IgdbFieldNames.FIRST_RELEASE_DATE)
    val firstReleaseDate: Long? = null
)

data class GameCover(
    @SerializedName(IgdbFieldNames.IMAGE_ID)
    val imageId: String
)


