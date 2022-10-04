package com.rosseti.itunessearch.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rosseti.domain.entity.ITunesEntity
import com.rosseti.itunessearch.ui.details.DetailsScreen
import com.rosseti.itunessearch.ui.home.HomeScreen
import com.rosseti.itunessearch.ui.home.HomeViewModel
import com.rosseti.itunessearch.ui.utils.Utils

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
            val search = it.savedStateHandle?.get<String>(Utils.SEARCH_TEXT_KEY)
            HomeScreen(navController = navController, viewModel = viewModel, search ?: "")
        }
        composable(AppScreens.DetailsScreen.name) {
            val entity = navController.previousBackStackEntry?.savedStateHandle?.get<ITunesEntity>(Utils.SONG_KEY)
            val search = navController.previousBackStackEntry?.savedStateHandle?.get<String>(Utils.SEARCH_TEXT_KEY)
            DetailsScreen(navController = navController, entity ?: ITunesEntity(), search ?: "")
        }
    }
}