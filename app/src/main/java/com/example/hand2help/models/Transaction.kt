package com.example.hand2help.models

data class Transaction(
    val transactionId: String = "",
    val name: String = "",
    val amount: String = "",
    val cause: String = "",
    val status: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
