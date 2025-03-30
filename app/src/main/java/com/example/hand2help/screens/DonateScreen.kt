package com.example.hand2help.screens

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun DonateScreen(navController: NavController) {
    var amount by remember { mutableStateOf("") }
    var paymentResult by remember { mutableStateOf<String?>(null) }

    val paymentLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.let { handlePaymentResponse(it) }
        } else {
            paymentResult = "Payment Failed"
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        TextField(value = amount, onValueChange = { amount = it }, label = { Text("Enter Amount") })
        Button(onClick = { startUPIPayment(amount, "yourupi@okicici", "Hand2Help", paymentLauncher) }) {
            Text("Donate via Google Pay")
        }
        paymentResult?.let {
            Text(text = it, color = if (it.contains("Success")) Color.Green else Color.Red)
        }
    }
}

// Start UPI Payment with Result Handling
fun startUPIPayment(amount: String, upiId: String, name: String, launcher: ActivityResultLauncher<Intent>) {
    val uri = Uri.parse("upi://pay").buildUpon()
        .appendQueryParameter("pa", upiId)
        .appendQueryParameter("pn", name)
        .appendQueryParameter("am", amount)
        .appendQueryParameter("cu", "INR")
        .build()
    val intent = Intent(Intent.ACTION_VIEW, uri)
    launcher.launch(intent)
}

// Handle Payment Response
fun handlePaymentResponse(data: Intent) {
    val response = data.getStringExtra("response") ?: "Transaction Failed"
    Log.d("UPI Response", response)

    if (response.contains("SUCCESS", ignoreCase = true)) {
        val transactionId = response.substringAfter("txnId=", "").substringBefore("&")
        val status = "Success (Txn ID: $transactionId)"
        saveTransactionToFirestore(transactionId, status)
    } else {
        Log.e("UPI Response", "Payment failed")
    }
}

// Save Transaction to Firestore
fun saveTransactionToFirestore(transactionId: String, status: String) {
    val db = FirebaseFirestore.getInstance()
    val donation = hashMapOf(
        "transactionId" to transactionId,
        "status" to status,
        "timestamp" to System.currentTimeMillis()
    )
    db.collection("transactions").add(donation)
}
