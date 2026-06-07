package com.example.vita_app.ui.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.vita_app.ui.screen.home.HomeScreen
import com.example.vita_app.ui.screen.login.LoginScreen
import com.example.vita_app.ui.screen.login.WelcomeScreen
import androidx.navigation.NavDestination.Companion.hasRoute
import com.example.vita_app.ui.components.BottomBar
import com.example.vita_app.ui.screen.diary.DiaryScreen


@SuppressLint("RestrictedApi")
@Composable
fun AppNavigation() {
    //INICIALIZAR NAV CONTROLLER
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val showBottomBar = remember(currentDestination) {
        currentDestination?.hierarchy?.any {
            it.hasRoute(Home::class) || it.hasRoute(Diary::class)
        } == true
    }

    Scaffold(
        bottomBar = {
            if (showBottomBar) BottomBar(navController)
        }
    ) { _ ->
        NavHost(
            navController = navController,
            startDestination = Welcome,
            modifier = Modifier.fillMaxSize()
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
                        navController.navigate(Home(name = "Steven")) {
                            popUpTo(Welcome) {
                                inclusive = true
                            } //Limpia el stack para que no se pueda regresar despues de entrar a Home
                        }
                    }
                )
            }

            //Pantalla de Home
            composable<Home> {
                HomeScreen()
            }

            composable<Diary> {
                DiaryScreen()
            }
        }
    }
}