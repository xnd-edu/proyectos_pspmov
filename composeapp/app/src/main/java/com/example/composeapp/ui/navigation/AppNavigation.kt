package com.example.composeapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.composeapp.ui.navigation.graphs.authNavGraph
import com.example.composeapp.ui.navigation.graphs.mainNavGraph
import com.example.composeapp.ui.navigation.routes.AuthGraph

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AuthGraph
    ) {
        authNavGraph(navController)

        mainNavGraph(navController)
    }
}

