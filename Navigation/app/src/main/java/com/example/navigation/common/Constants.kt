package com.example.navigation.common

object ApiConstants {
    const val JSON_PLACEHOLDER_BASE_URL = "https://jsonplaceholder.typicode.com/"
    const val TWITCH_AUTH_BASE_URL = "https://id.twitch.tv/"
    const val IGDB_BASE_URL = "https://api.igdb.com/v4/"
    const val IGDB_IMAGE_BASE_URL = "https://images.igdb.com/igdb/image/upload/"

    // IGDB
    const val IGDB_COVER_SIZE = "t_cover_big"
    const val IGDB_IMAGE_FORMAT = "webp"

    // Content Types
    const val CONTENT_TYPE_TEXT_PLAIN = "text/plain"
    const val HEADER_CLIENT_ID = "Client-ID"
    const val HEADER_AUTHORIZATION = "Authorization"
    const val BEARER_PREFIX = "Bearer "
}

object DaggerNames {
    const val TWITCH_AUTH = "TwitchAuth"
    const val IGDB = "Igdb"
    const val IGDB_OKHTTP = "IgdbOkHttp"
}

object FormatConstants {
    const val DATE_FORMAT_DD_MM_YYYY = "dd/MM/yyyy"
    const val RATING_FORMAT = "/100"
}

object IgdbQueryConstants {
    const val SEARCH_QUERY_TEMPLATE = "search \"%s\"; fields name, first_release_date, summary, rating, cover.image_id; limit %d;"
}

