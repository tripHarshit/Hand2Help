package com.example.hand2help.screens

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.hand2help.utils.FirebaseUtils
import com.example.hand2help.utils.PaymentUtils
import com.example.hand2help.models.Transaction

@Composable
fun DonateScreen(navController: NavController, cause: String) {
    var amount by remember { mutableStateOf("") }
    var paymentResult by remember { mutableStateOf<String?>(null) }

    val paymentLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.let {
                paymentResult = PaymentUtils.handlePaymentResponse(it).toString()
                if (paymentResult!!.contains("Success", ignoreCase = true)) {
                    val transactionId = paymentResult!!.substringAfter("Txn ID: ").substringBefore(" ")
                    saveTransactionToFirestore(transactionId, amount, cause, paymentResult!!)
                    navController.navigate("success")
                } else {
                    paymentResult = "Payment Failed"
                    navController.navigate("failure")
                }
            }
        } else {
            paymentResult = "Payment Failed"
            navController.navigate("failure")
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "Donate for: $cause", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(value = amount, onValueChange = { amount = it }, label = { Text("Enter Amount") })
        Spacer(modifier = Modifier.height(10.dp))
        Button(onClick = { PaymentUtils.startUPIPayment(amount, "success@upi", "Hand2Help", paymentLauncher) }) {
            Text("Donate via UPI")
        }
        Spacer(modifier = Modifier.height(10.dp))
        paymentResult?.let {
            Text(
                text = it,
                color = if (it.contains("Success", ignoreCase = true)) Color.Green else Color.Red,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

// Save Transaction to Firestore
fun saveTransactionToFirestore(transactionId: String, amount: String, cause: String, status: String) {
    val transaction = Transaction(
        transactionId = transactionId,
        name = FirebaseUtils.getCurrentUser()?.displayName ?: "Anonymous",
        amount = amount,
        cause = cause,
        status = status
    )

    FirebaseUtils.saveTransaction(transaction,
        onSuccess = { Log.d("Firestore", "Transaction saved successfully") },
        onFailure = { Log.e("Firestore", "Error saving transaction", it) }
    )
}
