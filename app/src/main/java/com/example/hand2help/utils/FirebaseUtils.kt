package com.example.hand2help.utils

import android.util.Log
import com.example.hand2help.models.Transaction
import com.google.firebase.firestore.FirebaseFirestore

object FirebaseUtils {
    private val db = FirebaseFirestore.getInstance()

    fun saveTransaction(transaction: Transaction) {
        db.collection("transactions")
            .document(transaction.transactionId)
            .set(transaction)
            .addOnSuccessListener {
                Log.d("Firebase", "Transaction saved successfully")
            }
            .addOnFailureListener { e ->
                Log.e("Firebase", "Error saving transaction", e)
            }
    }

    fun getUserTransactions(userId: String, callback: (List<Transaction>) -> Unit) {
        db.collection("transactions")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { documents ->
                val transactions = documents.map { it.toObject(Transaction::class.java) }
                callback(transactions)
            }
            .addOnFailureListener { e ->
                Log.e("Firebase", "Error getting transactions", e)
                callback(emptyList())
            }
    }
}
