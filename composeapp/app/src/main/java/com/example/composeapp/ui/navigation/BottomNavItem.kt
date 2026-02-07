package com.example.composeapp.ui.navigation

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.example.composeapp.R
import com.example.composeapp.ui.common.Constants

/**
 * Sealed class que representa los items del BottomNavigationBar
 */
sealed class BottomNavItem(
    val route: Any,
    val title: String,
    val icon: @Composable () -> Unit
) {
    data object ReindeersTab : BottomNavItem(
        route = Reindeers,
        title = Constants.REINDEERS_SCREEN,
        icon = {
            Icon(
                painter = painterResource(R.drawable.forest_24px),
                contentDescription = null
            )
        }
    )

    data object ProfileTab : BottomNavItem(
        route = Profile,
        title = Constants.PROFILE_SCREEN,
        icon = {
            Icon(
                painter = painterResource(R.drawable.person_24px),
                contentDescription = null
            )
        }
    )

    companion object {
        val items = listOf(ReindeersTab, ProfileTab)
    }
}

