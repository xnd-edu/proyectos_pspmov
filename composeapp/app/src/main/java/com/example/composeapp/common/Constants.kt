package com.example.composeapp.common

object ApiConstants {
    // 10.0.2.2 apunta al localhost de la máquina
    const val REINDEER_BASE_URL = "http://10.0.2.2:8080/"

    const val TWITCH_AUTH_BASE_URL = "https://id.twitch.tv/"
    const val IGDB_BASE_URL = "https://api.igdb.com/v4/"
    const val IGDB_IMAGE_BASE_URL = "https://images.igdb.com/igdb/image/upload/"

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
    const val DATE_FORMAT_YYYY = "yyyy"
    const val RATING_DECIMAL_FORMAT = "%.1f"
    const val TIMESTAMP_TO_MILLIS_MULTIPLIER = 1000L
}

object IgdbQueryConstants {
    const val SEARCH_QUERY_TEMPLATE = "search \"%s\"; fields name, first_release_date, summary, rating, cover.image_id; limit %d;"
    const val DEFAULT_SEARCH_LIMIT = 10
}

object IgdbFieldNames {
    const val ID = "id"
    const val NAME = "name"
    const val RATING = "rating"
    const val SUMMARY = "summary"
    const val COVER = "cover"
    const val FIRST_RELEASE_DATE = "first_release_date"
    const val IMAGE_ID = "image_id"
}

object ApiEndpoints {
    const val REINDEERS = "api/user/renos"
    const val REINDEERS_ADMIN = "api/admin/renos"
    const val REINDEER_BY_ID = "api/user/renos/{id}"
    const val REINDEER_BY_ID_ADMIN = "api/admin/renos/{id}"
    const val PATH_ID = "id"

    const val SECRETS = "api/secrets"
    const val SECRET_BY_ID = "api/secrets/{id}"

    const val SHARED_SECRETS = "api/secrets/share"
    const val SHARED_SECRET_BY_ID = "api/secrets/share/{id}"

    const val PUBLIC_KEY_BY_USERNAME = "api/public-keys/{username}"
    const val PATH_USERNAME = "username"

    const val GAMES = "games"

    const val AUTH_LOGIN = "login"
    const val AUTH_LOGOUT = "logout"
    const val AUTH_REGISTER = "register/noactivation"
}

object DataStoreConstants {
    const val AUTH_PREFS = "auth_prefs"
    const val ACCESS_TOKEN_KEY = "access_token"
    const val REFRESH_TOKEN_KEY = "refresh_token"
    const val USER_ROLE_KEY = "user_role"
    const val USER_ID_KEY = "user_id"
    const val USERNAME_KEY = "username"
    const val USER_EMAIL_KEY = "user_email"
    const val USER_NOMBRE_KEY = "user_nombre"
}

object UserRoles {
    const val ADMIN = "ADMIN"
    const val USER = "USER"
}

object HttpStatusCodes {
    const val UNAUTHORIZED = 401
    const val FORBIDDEN = 403
    const val NOT_FOUND = 404
    const val SERVER_ERROR = 500
}

object ErrorMessages {
    const val NO_KEY_PAIR = "No se encontró un par de claves para el usuario."
    const val PASSWORD_NOT_VALID = "La contraseña no es válida."
}

object CryptoConstants {
    const val ANDROID_KEYSTORE = "AndroidKeyStore"
    const val KEY_ALIAS = "MyUserIdentityKey" // Alias único para la clave del usuario
    const val RSA_ALGORITHM = "RSA"
    const val TRANSFORMATION_OAEP = "RSA/ECB/OAEPWithSHA-256AndMGF1Padding"
    const val SIGNATURE_ALGORITHM = "SHA256withRSA"
    const val RSA_KEY_SIZE = 2048
    const val SIGNATURE_FORMAT = "UserId:%d|PublicKey:%s|CreatedAt:%s"
    const val AES_ALGORITHM = "AES"
    const val TRANSFORMATION = "AES/GCM/NoPadding"
    const val AES_KEY_SIZE = 256
    const val GCM_TAG_LENGTH = 128
    const val PBKDF2_ITERATIONS = 65536
    const val IV_SIZE = 12
    const val SALT_SIZE = 16
    const val PBKDF2_ALGORITHM = "PBKDF2WithHmacSHA256"
}
