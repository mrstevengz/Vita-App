package com.example.vita_app.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.vita_app.ui.screen.home.HomeScreen
import com.example.vita_app.ui.screen.home.LoginScreen

@Composable
fun MainApp(modifier: Modifier) {

    var user by remember { mutableStateOf<String?>(null) }

    if (user == null) {
        LoginScreen(
            onLoginSuccess = { username ->
                user = username
            }
        )
    } else {
        HomeScreen(nombre = user!!)
    }
}