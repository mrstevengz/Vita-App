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

@Serializable object WelcomeRoute
@Serializable object LoginRoute
@Serializable data class HomeRoute(val name: String)
@Serializable data class DiaryRoute(val name: String)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = WelcomeRoute
    ) {
        // 1. Login Screen
        composable<LoginRoute> {
            LoginScreen(onLoginSuccess = { enteredName ->
                navController.navigate(HomeRoute(name = enteredName)) {
                    popUpTo(LoginRoute) { inclusive = true }
                }
            })
        }

// 2. Welcome Screen
        composable<WelcomeRoute> {
            WelcomeScreen(
                onLoginClick = { navController.navigate(LoginRoute) },
                onRegisterClick = {}
            )
        }

        // 3. Home Screen
        composable<HomeRoute> { backStackEntry ->
            val args = backStackEntry.toRoute<HomeRoute>()
            HomeScreen(name = args.name, navController = navController)
        }

        // 4. Diary Screen
        composable<DiaryRoute> { backStackEntry ->
            val args = backStackEntry.toRoute<DiaryRoute>()
            DiaryScreen(name = args.name, navController = navController)
        }
    }
}