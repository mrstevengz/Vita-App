package com.example.vita_app.ui.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.vita_app.ui.screen.home.HomeScreen
import com.example.vita_app.ui.screen.login.LoginScreen
import com.example.vita_app.ui.screen.login.WelcomeScreen
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.toRoute
import com.example.vita_app.ui.components.BottomBar
import com.example.vita_app.ui.screen.addmeal.AddMeal
import com.example.vita_app.ui.screen.diary.DiaryScreen
import com.example.vita_app.ui.screen.editmeal.EditMealScreen
import com.example.vita_app.ui.screen.meals.MealsViewModel


@SuppressLint("RestrictedApi")
@Composable
fun AppNavigation() {
    //INICIALIZAR NAV CONTROLLER
    val navController = rememberNavController()



    //Recuerda en que pantalla esta la app, vuelve a cambiar cada vez que cambia la pantalla
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    //Llama el navBackStackEntry y dice exactamente el composable en el que esta, no muestra otros argumentos
    val currentDestination = navBackStackEntry?.destination

    //Remember hace que calcule una vez  el destination, algo parecido a useMemo(),
    // hierarchy chequea todas las rutas existentes, si el destination
    //esta en la ruta Home o Diary, que actualize la variable a true, y asi se muestra el bottombar
    val showBottomBar = remember(currentDestination) {
        currentDestination?.hierarchy?.any {
            it.hasRoute(Home::class) || it.hasRoute(Diary::class)
        } == true //== true hace que acepte nulos como verdaderos, ya que puede haber nulos con el ?
    }

    //Se inicializa un viewmodel meals.
    val mealsViewModel: MealsViewModel = viewModel()

    val snackbarHostState = remember {
        SnackbarHostState()
    }

    LaunchedEffect(Unit) {
        mealsViewModel.events.collect { message -> snackbarHostState.showSnackbar(message) }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState)},
        bottomBar = {
            if (showBottomBar) BottomBar(navController)
        }
    ) { _ -> //Ignoro el padding de scaffold con _, para darle su padding propio a todas las pantallas
        //en AppBackground y que sean consistentes
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
                DiaryScreen(
                    viewModel = mealsViewModel,
                    onAddMealClick = {navController.navigate(AddMeal)},
                    onMealEditClick = {id -> navController.navigate(EditMeal(id))}
                )
            }

            composable<AddMeal> {
                AddMeal(
                    viewModel = mealsViewModel,
                    onMealAdd = {navController.popBackStack() }
                )
            }

            //Pantalla de Edit
            composable<EditMeal> {
                entry -> val args = entry.toRoute<EditMeal>()
                EditMealScreen(
                    vm = mealsViewModel,
                    mealId = args.mealId,
                    onCompleted = {navController.popBackStack()}
                )
            }
        }
    }
}