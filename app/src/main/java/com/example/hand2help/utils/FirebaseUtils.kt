package com.example.hand2help.utils


import com.example.hand2help.models.Donation
import com.example.hand2help.models.Transaction
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

object FirebaseUtils {
    val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    val firestore: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    // Function to get current user
    fun getCurrentUser() = auth.currentUser

    // Save donation to Firestore
    fun saveDonation(donation: Donation, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        firestore.collection("donations").add(donation)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }

    // Save transaction to Firestore
    fun saveTransaction(transaction: Transaction, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        firestore.collection("transactions").add(transaction)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }
}
