package com.mk.dehazing.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mk.dehazing.ui.presentation.home.HomeScreen

@Composable
fun Navigation(

) {
    val controller = rememberNavController()

    NavHost(
        navController = controller,
        startDestination = NavScreen.Home.route
    ) {
        composable (
            route = NavScreen.Home.route
        ) {
            HomeScreen()
        }
    }
}