package com.example.hand2help.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun HomeScreen(navController: NavController) {
    val causes = listOf("Education", "Healthcare", "Environment", "Disaster Relief", "Animal Welfare")

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "Choose a Cause", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(causes) { cause ->
                CauseButton(cause) {
                    val encodedCause = URLEncoder.encode(cause, StandardCharsets.UTF_8.toString())
                    navController.navigate("donate/$encodedCause")
                }
            }
        }
    }
}

@Composable
fun CauseButton(cause: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
    ) {
        Text(text = cause, fontSize = 18.sp)
    }
}
