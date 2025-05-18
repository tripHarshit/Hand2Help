package com.example.hand2help

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.hand2help.models.Transaction
import com.example.hand2help.navigation.AppNavigation
import com.example.hand2help.ui.theme.Hand2HelpTheme
import com.example.hand2help.utils.FirebaseUtils
import com.google.firebase.FirebaseApp
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import org.json.JSONObject
import java.util.UUID

class MainActivity : ComponentActivity(), PaymentResultListener {

    private var currentAmount = ""
    private var currentCause = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Ensure Firebase is initialized
        if (!isFirebaseInitialized()) {
            FirebaseApp.initializeApp(this)
        }

        // Initialize Razorpay
        Checkout.preload(applicationContext)

        // Set the content of the app
        setContent {
            Hand2HelpTheme {
                AppNavigation(
                    activity = this
                )
            }
        }
    }

    private fun isFirebaseInitialized(): Boolean {
        return FirebaseApp.getApps(this).isNotEmpty()
    }

    fun startRazorpayPayment(amount: String, cause: String) {
        currentAmount = amount
        currentCause = cause

        val activity = this
        val checkout = Checkout()

        // Set your Razorpay key ID
        checkout.setKeyID("rzp_test_7PjW41Vg1P07H3")

        try {
            val options = JSONObject()
            options.put("name", "Hand2Help")
            options.put("description", "Donation for $cause")
            options.put("currency", "INR")
            options.put("amount", amount.toInt() * 100) // amount in paisa
            options.put("prefill.email", "user@example.com")
            options.put("prefill.contact", "9876543210")
            options.put("theme.color", "#0D5CB4")

            checkout.open(activity, options)
        } catch (e: Exception) {
            Log.e("Razorpay", "Error in starting payment", e)
            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }


    override fun onPaymentSuccess(razorpayPaymentId: String) {
        // Payment successful
        Log.d("Razorpay", "Payment successful: $razorpayPaymentId")

        // Create transaction record in Firebase
        val transaction = Transaction(
            transactionId = razorpayPaymentId,
            name = "User Name",
            amount = currentAmount,
            cause = currentCause,
            status = "COMPLETED"
        )

        FirebaseUtils.saveTransaction(transaction)


       // Toast.makeText(this, "Payment Successful: $razorpayPaymentId", Toast.LENGTH_LONG).show()
    }

    override fun onPaymentError(code: Int, description: String) {
        Log.e("Razorpay", "Payment failed: $description")
       // Toast.makeText(this, "Payment Failed: $description", Toast.LENGTH_LONG).show()
    }
}
