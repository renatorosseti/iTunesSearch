package com.rosseti.itunessearch.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.rosseti.itunessearch.ui.home.HomeScreen
import com.rosseti.itunessearch.ui.home.HomeViewModel


@ExperimentalComposeUiApi
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = AppScreens.MainScreen.name
    ) {
        composable(AppScreens.MainScreen.name) {
            val viewModel: HomeViewModel = hiltViewModel()
            HomeScreen(navController = navController, viewModel = viewModel)
        }
        composable(AppScreens.DetailsScreen.name) {

        }
        composable(
            AppScreens.DetailsScreen.name + "/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->

        }
    }
}

fun NavBackStackEntry.getString(value: String) = this.arguments?.getString(value) ?: ""