package com.example.hand2help.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun FailureScreen(navController: NavController) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "❌ Payment Failed!", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.Red)
        Button(onClick = { navController.navigate("home") }) {
            Text(text = "Try Again")
        }
    }
}