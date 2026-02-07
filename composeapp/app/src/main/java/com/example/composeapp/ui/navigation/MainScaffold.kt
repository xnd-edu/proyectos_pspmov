package com.example.composeapp.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.composeapp.ui.LoginScreen
import com.example.composeapp.ui.screens.coches.CarFormScreenVM

@Composable
fun MainScaffold() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        bottomBar = {
            // Solo mostrar el BottomNav si NO estamos en DetailScreen
            val shouldShowBottomBar = currentDestination?.hierarchy?.any {
                it.hasRoute(Detail::class)
                it.hasRoute(Login::class)
            } != true

            if (shouldShowBottomBar) {
                BottomNavigationBar(
                    navController = navController,
                    currentDestination = currentDestination
                )
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Login,
            modifier = Modifier.padding(paddingValues)
        ) {
            // Tab 1: Login
            composable<Login> {
                LoginScreen(

                    navigateToHome = {
                        // Navegar a Home manteniendo Login en la pila
                        navController.navigate(Reindeers) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }

            // Tab 2: User
            composable<Reindeers> {
                CarFormScreenVM()
            }

            // Tab 3: Home
//            composable<Profile> {
//                HomeScreen { name ->
//                    navController.navigate(Detail(name = name))
//                }
//            }

            // Pantalla Detail (fuera de tabs)
//            composable<Detail> { backStackEntry ->
//                val detail: Detail = backStackEntry.toRoute()
//                DetailScreen(
//                    name = detail.name,
//                    navigateBack = {
//                        // Volver atrás simple
//                        navController.popBackStack()
//
//                        /* Otras opciones disponibles:
//
//                        // Opción 2: Volver a Login limpiando todo
//                        navController.navigate(Login) {
//                            popUpTo(Login) {
//                                inclusive = true
//                            }
//                            launchSingleTop = true
//                            restoreState = true
//                        }
//
//                        // Opción 3: Volver a Home
//                        navController.navigate(Home) {
//                            popUpTo(Home) {
//                                inclusive = true
//                            }
//                            launchSingleTop = true
//                        }
//                        */
//                    }
//                )
//            }
        }
    }
}

@Composable
fun BottomNavigationBar(
    navController: NavHostController,
    currentDestination: androidx.navigation.NavDestination?
) {
    NavigationBar {
        BottomNavItem.items.forEach { item ->
            val isSelected = currentDestination?.hierarchy?.any {
                it.hasRoute(item.route::class)
            } == true

            NavigationBarItem(
                icon = { item.icon() },
                label = { Text(item.title) },

                selected = isSelected,
                onClick = {
                    navController.navigate(item.route) {
                        // Pop hasta el inicio del grafo para evitar acumular pantallas
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        // Evitar múltiples copias del mismo destino
                        launchSingleTop = true
                        // Restaurar el estado al volver a un tab previamente visitado
                        restoreState = true
                    }
                }
            )
        }
    }
}

