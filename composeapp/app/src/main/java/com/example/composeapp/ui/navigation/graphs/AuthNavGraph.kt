package com.example.composeapp.ui.navigation.graphs

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.composeapp.ui.navigation.routes.AuthGraph
import com.example.composeapp.ui.navigation.routes.Login
import com.example.composeapp.ui.navigation.routes.MainGraph
import com.example.composeapp.ui.navigation.routes.Register
import com.example.composeapp.ui.screens.auth.login.LoginScreenVM
import com.example.composeapp.ui.screens.auth.register.RegisterScreenVM

fun NavGraphBuilder.authNavGraph(
    navController: NavHostController
) {
    navigation<AuthGraph>(
        startDestination = Login
    ) {
        composable<Login> {
            AuthScaffold(
                navController = navController
            )
        }

        composable<Register> {
            Scaffold { paddingValues ->
                RegisterScreenVM(
                    modifier = Modifier.padding(paddingValues),
                    navigateToReindeers = {
                        navController.navigate(MainGraph) {
                            popUpTo(AuthGraph) { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    navigateBack = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}

@Composable
private fun AuthScaffold(
    navController: NavHostController
) {
    Scaffold { paddingValues ->
        LoginScreenVM(
            modifier = Modifier.padding(paddingValues),
            navigateToReindeers = {
                navController.navigate(MainGraph) {
                    popUpTo(AuthGraph) { inclusive = true }
                    launchSingleTop = true
                }
            },
            navigateToRegister = {
                navController.navigate(Register)
            }
        )
    }
}


