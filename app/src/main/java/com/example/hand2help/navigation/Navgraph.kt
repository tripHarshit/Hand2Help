package com.example.hand2help.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
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
  val context = LocalContext.current
    NavHost(navController, startDestination = "home") {
        composable("home") { HomeScreen(navController) }
        composable("donate/{cause}") { backStackEntry ->
            DonateScreen(
                navController, backStackEntry.arguments?.getString("cause") ?: "Unknown",
                context = context
            )
        }
        composable("success") { SuccessScreen(navController) }
        composable("failure") { FailureScreen(navController) }
    }
}
