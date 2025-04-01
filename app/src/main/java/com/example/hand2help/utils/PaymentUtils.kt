package com.example.hand2help.utils

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.core.net.toUri
import java.net.URLEncoder

import kotlin.random.Random
import kotlin.random.nextInt

object PaymentUtils {
    private const val TAG = "UPIPayment"

    fun startUPIPayment(amount: String, upiId: String, name: String, launcher: ActivityResultLauncher<Intent>) {
        if (!isValidUpiId(upiId)) {
            Log.e(TAG, "Invalid UPI ID format: $upiId")
           // showToast("Invalid UPI ID format. Please check recipient details.")
            return
        }

        val uri = buildPaymentUri(
            upiId = upiId,
            name = name,
            amount = amount,
            transactionRef = "H2H${System.currentTimeMillis() % 1000000}"
        )

        Log.d(TAG, "Initiating payment with URI: $uri")
        launchPaymentIntent(uri, launcher)
    }

    private fun isValidUpiId(upiId: String): Boolean {
        return upiId.matches("^[a-zA-Z0-9.-]{2,256}@[a-zA-Z]{2,64}$".toRegex())
    }

    private fun buildPaymentUri(
        upiId: String,
        name: String,
        amount: String,
        transactionRef: String
    ): Uri {
        return Uri.parse("upi://pay").buildUpon().apply {
            appendQueryParameter("pa", upiId)
            appendQueryParameter("pn", name)
            appendQueryParameter("tr", transactionRef)
            appendQueryParameter("tn", "Donation")
            appendQueryParameter("am", amount)
            appendQueryParameter("cu", "INR")
            appendQueryParameter("mc", "")
            appendQueryParameter("url", "")
        }.build()
    }

    private fun launchPaymentIntent(uri: Uri, launcher: ActivityResultLauncher<Intent>) {
        try {
            Intent(Intent.ACTION_VIEW, uri).apply {
                // Try these UPI apps in order
                listOf(
                    "com.google.android.apps.nbu.paisa.user", // Google Pay
                    "in.org.npci.upiapp",                     // BHIM
                    "net.one97.paytm",                        // PayTM
                    "com.phonepe.app"                         // PhonePe
                ).forEach { pkg ->
                    setPackage(pkg)
                    try {
                        launcher.launch(this)
                        return
                    } catch (e: ActivityNotFoundException) {
                        Log.d(TAG, "$pkg not installed")
                    }
                }

                // Fallback to any UPI app
                setPackage(null)
                launcher.launch(this)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Payment launch failed", e)
         //   showToast("No UPI app found. Please install Google Pay or other UPI apps.")
        }
    }

    fun handlePaymentResponse(data: Intent?): PaymentResult {
        if (data == null) return PaymentResult.Cancelled("Payment cancelled by user")

        val status = data.getStringExtra("Status") ?: "NO_STATUS"
        val response = data.getStringExtra("response") ?: ""

        return when (status.uppercase()) {
            "SUCCESS" -> PaymentResult.Success(
                transactionId = extractTransactionId(response)
            )
            "FAILURE" -> when {
                response.contains("limit exceeded", ignoreCase = true) ->
                    PaymentResult.Failed("Bank transaction limit exceeded")
                else -> PaymentResult.Failed("Payment failed: ${response.take(50)}")
            }
            else -> PaymentResult.Unknown("Unknown status: $status")
        }
    }

    private fun extractTransactionId(response: String): String {
        return listOf(
            Regex("txnId=([^&]+)"),
            Regex("txnRef=([^&]+)"),
            Regex("transactionId=([^&]+)")
        ).firstNotNullOfOrNull { it.find(response)?.groupValues?.getOrNull(1) }
            ?: "N/A"
    }

    sealed class PaymentResult {
        data class Success(val transactionId: String) : PaymentResult()
        data class Failed(val message: String) : PaymentResult()
        data class Cancelled(val message: String) : PaymentResult()
        data class Unknown(val message: String) : PaymentResult()
    }
}