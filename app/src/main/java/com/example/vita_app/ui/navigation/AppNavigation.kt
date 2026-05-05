package com.example.vita_app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.vita_app.ui.screen.home.DiaryScreen
import com.example.vita_app.ui.screen.home.HomeScreen
import com.example.vita_app.ui.screen.home.LoginScreen
import com.example.vita_app.ui.screen.home.WelcomeScreen
import kotlinx.serialization.Serializable

// AppNavigation.kt

// Estas clases representan las rutas de navegación
// Se usan en lugar de Strings para evitar errores y manejar argumentos fácilmente
@Serializable object WelcomeRoute
@Serializable object LoginRoute
@Serializable data class HomeRoute(val name: String)
@Serializable data class DiaryRoute(val name: String)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    // NavHost es el contenedor donde viven todas las pantallas
    NavHost(
        navController = navController,
        startDestination = WelcomeRoute
    ) {
        // 1. Login Screen
        composable<LoginRoute> {
            LoginScreen(onLoginSuccess = { enteredName ->
                navController.navigate(HomeRoute(name = enteredName)) {   // Navegamos a Home enviando el nombre del usuario, solo pasa si el loggin es exitoso
                    popUpTo(LoginRoute) { inclusive = true }
                    // Eliminamos Login del historial
                    // para que el usuario no pueda volver con el botón atrás
                }
            })
        }

// 2. Welcome Screen
        composable<WelcomeRoute> {
            WelcomeScreen(
                // Si presiona login → va a LoginScreen
                onLoginClick = { navController.navigate(LoginRoute) },
                // Registro (aún no implementado)
                onRegisterClick = {}
            )
        }

        // 3. Home Screen
        composable<HomeRoute> { backStackEntry ->
            val args = backStackEntry.toRoute<HomeRoute>()
            // Mostramos la pantalla Home con el nombre
            // También pasamos el navController para navegar desde ahí
            HomeScreen(name = args.name, navController = navController)
        }

        // 4. Diary Screen
        // Recuperamos los datos enviados (el nombre)
        composable<DiaryRoute> { backStackEntry ->
            val args = backStackEntry.toRoute<DiaryRoute>()
            DiaryScreen(name = args.name, navController = navController) // mostramos la pantalla diary
        }
    }
}