package com.example.composeapp.ui.navigation.graphs

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MediumExtendedFloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShortNavigationBar
import androidx.compose.material3.ShortNavigationBarItem
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navigation
import com.example.composeapp.R
import com.example.composeapp.ui.common.Constants
import com.example.composeapp.ui.navigation.BottomNavItem
import com.example.composeapp.ui.navigation.routes.AddReindeer
import com.example.composeapp.ui.navigation.routes.AuthGraph
import com.example.composeapp.ui.navigation.routes.EditReindeer
import com.example.composeapp.ui.navigation.routes.Games
import com.example.composeapp.ui.navigation.routes.MainGraph
import com.example.composeapp.ui.navigation.routes.Profile
import com.example.composeapp.ui.navigation.routes.Reindeers
import com.example.composeapp.ui.screens.games.GamesMainScreenVM
import com.example.composeapp.ui.screens.profile.ProfileScreenVM
import com.example.composeapp.ui.screens.reindeers.add.ReindeerAddScreenVM
import com.example.composeapp.ui.screens.reindeers.edit.ReindeerEditScreenVM
import com.example.composeapp.ui.screens.reindeers.main.ReindeerMainScreenVM
import com.example.composeapp.ui.screens.reindeers.main.ReindeerMainViewModel
import com.example.composeapp.ui.theme.Dimens


fun NavGraphBuilder.mainNavGraph(
    navController: NavHostController
) {
    navigation<MainGraph>(
        startDestination = Reindeers
    ) {

        composable<Reindeers> {
            MainScaffoldWithBottomBar(
                navController = navController,
                currentRoute = Reindeers::class
            )
        }

        composable<Games> {
            MainScaffoldWithBottomBar(
                navController = navController,
                currentRoute = Games::class
            )
        }

        composable<Profile> {
            MainScaffoldWithBottomBar(
                navController = navController,
                currentRoute = Profile::class
            )
        }

        composable<AddReindeer> {
            DetailScaffold { snackbarHostState, modifier ->
                ReindeerAddScreenVM(
                    modifier = modifier,
                    snackbarHostState = snackbarHostState,
                    navigateBack = { navController.popBackStack() }
                )
            }
        }

        composable<EditReindeer> { _ ->
            DetailScaffold { snackbarHostState, modifier ->
                ReindeerEditScreenVM(
                    modifier = modifier,
                    snackbarHostState = snackbarHostState,
                    navigateBack = { navController.popBackStack() }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun MainScaffoldWithBottomBar(
    navController: NavHostController,
    currentRoute: kotlin.reflect.KClass<*>
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val snackbarHostState = remember { SnackbarHostState() }

    // Obtener el ViewModel solo si estamos en Reindeers para acceder a isAdmin
    val reindeerViewModel: ReindeerMainViewModel? = if (currentRoute == Reindeers::class) {
        hiltViewModel()
    } else null

    val isAdmin = reindeerViewModel?.state?.collectAsStateWithLifecycle()?.value?.isAdmin ?: false

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        bottomBar = {
            BottomNavigationBar(
                navController = navController,
                currentDestination = currentDestination
            )
        },
        floatingActionButton = {
            // Mostrar FAB solo en Reindeers y si el usuario es admin
            if (currentRoute == Reindeers::class && isAdmin) {
                MediumExtendedFloatingActionButton(
                    onClick = { navController.navigate(AddReindeer) }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.add_24px),
                        contentDescription = stringResource(R.string.añadir),
                        modifier = Modifier.size(FloatingActionButtonDefaults.MediumIconSize)
                    )
                    Spacer(Modifier.width(Dimens.spacingSmall))
                    Text(text = stringResource(R.string.añadir))
                }
            }
        }
    ) { paddingValues ->
        when (currentRoute) {
            Reindeers::class -> {
                ReindeerMainScreenVM(
                    modifier = Modifier.padding(paddingValues),
                    viewModel = reindeerViewModel ?: hiltViewModel(),
                    navigateToDetail = { id ->
                        if (id == Constants.ADD_SCREEN) {
                            navController.navigate(AddReindeer)
                        } else {
                            navController.navigate(EditReindeer(id = id.toInt()))
                        }
                    }
                )
            }
            Games::class -> {
                GamesMainScreenVM(
                    modifier = Modifier.padding(paddingValues)
                )
            }
            Profile::class -> {
                ProfileScreenVM(
                    modifier = Modifier.padding(paddingValues),
                    navigateToLogin = {
                        navController.navigate(AuthGraph) {
                            popUpTo(0) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun DetailScaffold(
    content: @Composable (SnackbarHostState, Modifier) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { paddingValues ->
        content(snackbarHostState, Modifier.padding(paddingValues))
    }
}

@Composable
fun BottomNavigationBar(
    navController: NavHostController,
    currentDestination: androidx.navigation.NavDestination?
) {
    ShortNavigationBar {
        BottomNavItem.items.forEach { item ->
            val isSelected = currentDestination?.hierarchy?.any {
                it.hasRoute(item.route::class)
            } == true

            ShortNavigationBarItem(
                icon = { item.icon() },
                label = { Text(item.title) },
                selected = isSelected,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}





