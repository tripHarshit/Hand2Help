package com.example.hand2help.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.hand2help.MainActivity
import com.example.hand2help.screens.DonateScreen
import com.example.hand2help.screens.HomeScreen

@Composable
fun AppNavigation(
    activity: MainActivity
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(navController = navController)
        }
        composable(
            route = "donate/{cause}",
            arguments = listOf(navArgument("cause") { type = NavType.StringType })
        ) { backStackEntry ->
            val cause = backStackEntry.arguments?.getString("cause") ?: "Unknown Cause"
            DonateScreen(
                navController = navController,
                cause = cause,
                activity = activity
            )
        }
    }
}
