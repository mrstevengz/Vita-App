package com.example.vita_app.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.vita_app.ui.navigation.DiaryRoute
import com.example.vita_app.ui.navigation.HomeRoute

@Composable
fun BottomBar(navController: NavHostController, currentName: String) {
    NavigationBar {
        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate(HomeRoute(name = currentName)) },
            icon = { Icon(Icons.Default.Home, contentDescription = null) },
            label = { Text("Home") }
        )
        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate(DiaryRoute(name = currentName)) },
            icon = { Icon(Icons.Default.Book, contentDescription = null) },
            label = { Text("Diary") }
        )
    }
}