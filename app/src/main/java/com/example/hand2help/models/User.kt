package com.example.hand2help.models

data class User(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val profileImage: String = "" // URL from Google Sign-In
)
