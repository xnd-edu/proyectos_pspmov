package com.example.composeapp.ui.common

object Constants {
    const val REINDEERS_SCREEN = "Renos"
    const val PROFILE_SCREEN = "Perfil"
    const val GAMES_SCREEN = "Juegos"

    const val ADD_SCREEN = "add"

    // Preview data constants
    const val PREVIEW_GAME_NAME_1 = "The Legend of Zelda: Breath of the Wild"
    const val PREVIEW_GAME_NAME_2 = "Super Mario Odyssey"
    const val PREVIEW_GAME_NAME_3 = "God of War"
    const val PREVIEW_SEARCH_QUERY_ZELDA = "zelda"
    const val PREVIEW_SEARCH_QUERY_EMPTY = ""

    // Login preview data
    const val PREVIEW_USERNAME = "tebashdp"
    const val PREVIEW_PASSWORD = "contraseña"

    // Profile preview data
    const val PREVIEW_USER_ID = 1
    const val PREVIEW_USER_USERNAME = "admin"
    const val PREVIEW_USER_EMAIL = "admin@example.com"
    const val PREVIEW_USER_NOMBRE = "Administrator"

    // Reindeer preview data
    const val PREVIEW_REINDEER_NAME_1 = "Rudolph"
    const val PREVIEW_REINDEER_NAME_2 = "Dasher"
    const val PREVIEW_REINDEER_NAME_3 = "Dancer"
    const val PREVIEW_REINDEER_COLOR_1 = "Rojo"
    const val PREVIEW_REINDEER_COLOR_2 = "Marrón"
    const val PREVIEW_REINDEER_COLOR_3 = "Café"
}

/**
 * Breakpoints de ventana según Material Design 3
 * hhttps://developer.android.com/develop/ui/compose/layouts/adaptive/use-window-size-classes
 */
object WindowBreakpoints {
    // Width breakpoints (dp)
    const val WIDTH_COMPACT = 0      // < 600dp
    const val WIDTH_MEDIUM = 600     // 600dp - 839dp
    const val WIDTH_EXPANDED = 840   // 840dp - 1199dp
    const val WIDTH_LARGE = 1200     // 1200dp - 1599dp
    const val WIDTH_EXTRA_LARGE = 1600 // >= 1600dp

    // Height breakpoints (dp)
    const val HEIGHT_COMPACT = 0     // < 480dp
    const val HEIGHT_MEDIUM = 480    // 480dp - 899dp
    const val HEIGHT_EXPANDED = 900  // >= 900dp
}

