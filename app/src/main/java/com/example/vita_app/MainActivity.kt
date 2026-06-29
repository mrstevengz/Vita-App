package com.example.vita_app

// Proposito: Punto de entrada de la app Android. Carga Jetpack Compose y abre la navegacion principal.

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.vita_app.ui.navigation.AppNavigation
import com.example.vita_app.ui.theme.VitaAppTheme

// Activity principal: Android la abre porque esta declarada como LAUNCHER en AndroidManifest.xml.
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // setContent inicia la interfaz declarativa de Jetpack Compose.
        setContent {
            VitaAppTheme {
                AppNavigation()
            }
        }
    }
}