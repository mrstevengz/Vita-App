package com.example.vita_app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.vita_app.ui.screen.addmeal.AddMeal
import com.example.vita_app.ui.screen.diary.DiaryScreen
import com.example.vita_app.ui.screen.home.HomeScreen
import com.example.vita_app.ui.screen.login.LoginScreen
import com.example.vita_app.ui.screen.login.WelcomeScreen
import kotlinx.serialization.Serializable


@Composable
fun AppNavigation() {
    //INICIALIZAR NAV CONTROLLER
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Welcome
    ) {
        //Pantalla de bienvenida

        composable<Welcome> {
            WelcomeScreen(
                onNavigateToLogin = {
                    navController.navigate(Login) //Navega a la pantalla de Login
                },
                onRegisterClick = {
                    Unit //Falta la pantalla de registro y auth (lo dejo para el final)
                }
            )
        }

        //Pantalla de Login

        composable<Login> {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Home) {
                        popUpTo(Welcome) { inclusive = true  } //Limpia el stack para que no se pueda regresar despues de entrar a Home
                    }
                }
            )
        }

        //Pantalla de Home
    }
}