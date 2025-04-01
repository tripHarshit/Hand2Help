package com.example.hand2help.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.hand2help.screens.DonateScreen
import com.example.hand2help.screens.FailureScreen
import com.example.hand2help.screens.HomeScreen
import com.example.hand2help.screens.SuccessScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomeScreen(navController) }
        composable("donate/{cause}") { backStackEntry ->
            val cause = backStackEntry.arguments?.getString("cause") ?: "Unknown"
            DonateScreen(navController, cause)
        }
        composable("success") { SuccessScreen(navController) }
        composable("failure") { FailureScreen(navController) }
    }
}
