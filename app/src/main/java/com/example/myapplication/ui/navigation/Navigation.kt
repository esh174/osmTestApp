package com.example.myapplication.ui.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.myapplication.ui.pinPointMap.PinPointMapScreen
import com.example.myapplication.ui.pinPointMap.PinPointMapViewModel

sealed class NavigationRoutes(
    val route: String,
) {
    data object Main : NavigationRoutes("main") {
        fun createRoute() = "main"
    }
}

@Composable
fun Navigation(
    navController: NavHostController,
    startDestination: String,
) {
    NavHost(
        modifier = Modifier.fillMaxSize(),
        navController = navController,
        startDestination = startDestination,
        enterTransition = { fadeIn(tween(400)) },
        exitTransition = { fadeOut(tween(400)) },
    ) {
        composable(
            route = NavigationRoutes.Main.route
        ) {
            val viewModel = hiltViewModel<PinPointMapViewModel>()

            PinPointMapScreen(
                viewModel = viewModel,
                navController = navController
            )
        }
    }
}