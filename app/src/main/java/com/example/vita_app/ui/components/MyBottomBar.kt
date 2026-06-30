package com.example.vita_app.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.vita_app.ui.navigation.Diary
import com.example.vita_app.ui.navigation.Home

@Composable
fun BottomBar(navController: NavController) {
    //Se vuelven a llamar las mismas variables del AppNavigation, para saber en que pantalla se esta
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    //Componente default para tener una barra de navegacion abajo
    NavigationBar {
        NavigationBarItem(
            //El selected pregunta si de todas las rutas que existen, current destination es Home,
            //si lo es, devuelve el ==true para pasar nulos y hacer que se muestre como seleccionado
            selected = currentDestination?.hierarchy?.any {
                it.hasRoute(Home::class)
            } == true,
            onClick = { navController.navigate(Home("Yo")) {
                launchSingleTop = true //Hace que solo se guarde una vez la pantalla en el stack
                restoreState = true // Al regresar a una pantalla con el back, restaurar su scroll/inputs
                popUpTo(navController.graph.startDestinationId) { //Guardar el estado del tab para luego restaurarlo
                    saveState = true
                }
            } },
            icon = { Icon(Icons.Default.Home, contentDescription = null) },
            label = { Text("Home") }
        )
        NavigationBarItem(
            selected = currentDestination?.hierarchy?.any {
                it.hasRoute(Diary::class)
            } == true,
            onClick = { navController.navigate(Diary("Yo")) {
                launchSingleTop = true
                restoreState = true
                popUpTo(navController.graph.startDestinationId) {
                    saveState = true
                }
            } },
            icon = { Icon(Icons.Default.Dashboard, contentDescription = null) },
            label = { Text("Diary") }
        )
    }
}