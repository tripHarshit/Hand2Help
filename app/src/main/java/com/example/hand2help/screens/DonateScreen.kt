package com.example.hand2help.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.hand2help.MainActivity

@Composable
fun DonateScreen(
    navController: NavController,
    cause: String,
    activity: MainActivity
) {
    var amount by remember { mutableStateOf("100") }
    var isProcessing by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Donate to $cause",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        OutlinedTextField(
            value = amount,
            onValueChange = {
                if (it.isEmpty() || it.all { char -> char.isDigit() }) {
                    amount = it
                }
            },
            label = { Text("Enter Amount (INR)") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        )

        Button(
            onClick = {
                Log.d("Razorpay", "Button clicked with amount: $amount")
                if (amount.isNotEmpty() && amount.toIntOrNull() != null && amount.toInt() > 0) {
                    isProcessing = true
                    showError = false
                    // Call Razorpay payment method
                    activity.startRazorpayPayment(amount, cause)
                } else {
                    showError = true
                    errorMessage = "Please enter a valid amount"
                }
            },
            enabled = !isProcessing,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(text = "Donate Now")
        }

        if (showError) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}
