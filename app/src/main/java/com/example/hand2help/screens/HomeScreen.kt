package com.example.hand2help.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun HomeScreen(navController: NavController) {
    val causes = listOf("Education", "Healthcare", "Environment")

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Choose a Cause", fontSize = 24.sp, fontWeight = FontWeight.Bold)

        causes.forEach { cause ->
            Button(onClick = {
                navController.navigate("donate/$cause")
            }) {
                Text(text = cause)
            }
        }
    }
}
