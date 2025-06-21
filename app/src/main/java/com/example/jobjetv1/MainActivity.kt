package com.example.jobjetv1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat // Import WindowCompat
import com.example.jobjetv1.navigation.AppNavHost

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set decorFitsSystemWindows to true to make room for the status bar
        WindowCompat.setDecorFitsSystemWindows(window, true) // Add this line
        setContent { AppNavHost() }
    }
}