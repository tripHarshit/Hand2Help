package com.example.hand2help.models

data class Donation(
    val transactionId: String = "",
    val donorId: String = "",
    val donorName: String = "",
    val amount: String = "",
    val cause: String = "",
    val status: String = "Pending",
    val timestamp: Long = System.currentTimeMillis()
)
