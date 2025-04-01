package com.example.hand2help

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Scaffold
import com.example.hand2help.navigation.AppNavigation
import com.example.hand2help.ui.theme.Hand2HelpTheme
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Ensure Firebase is initialized only once
        if (!isFirebaseInitialized()) {
            FirebaseApp.initializeApp(this)
        }

        enableEdgeToEdge()
        setContent {
            Hand2HelpTheme {
                Scaffold { innerPadding ->
                    AppNavigation()
                }
            }
        }
    }

    // Function to check if Firebase is already initialized
    private fun isFirebaseInitialized(): Boolean {
        return FirebaseApp.getApps(this).isNotEmpty()
    }
}
