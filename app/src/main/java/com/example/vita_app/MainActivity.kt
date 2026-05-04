package com.example.vita_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.vita_app.ui.navigation.AppNavigation
import com.example.vita_app.ui.theme.VitaAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VitaAppTheme {
                AppNavigation()
            }
        }
    }
}