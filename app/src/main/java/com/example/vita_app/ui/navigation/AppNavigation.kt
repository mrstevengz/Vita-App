package com.example.vita_app.ui.navigation

// Proposito: Coordina la navegacion, los ViewModels compartidos, la BottomBar y los mensajes Snackbar.

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
import com.example.vita_app.ui.screen.catalog.CatalogScreen
import com.example.vita_app.ui.screen.diary.DiaryScreen
import com.example.vita_app.ui.screen.editmeal.EditMealScreen
import com.example.vita_app.ui.screen.login.AuthEvent
import com.example.vita_app.ui.screen.login.AuthViewModel
import com.example.vita_app.ui.screen.login.RegisterScreen
import com.example.vita_app.ui.screen.meals.MealsViewModel
import com.example.vita_app.ui.screen.workouts.WorkoutCatalogScreen
import com.example.vita_app.ui.screen.workouts.WorkoutViewModel
import java.util.Map.entry


@SuppressLint("RestrictedApi")
@Composable
// Composable central donde se definen pantallas y acciones de navegacion.
fun AppNavigation() {
    //INICIALIZAR NAV CONTROLLER
    val navController = rememberNavController()



    //Recuerda en que pantalla esta la app, vuelve a cambiar cada vez que cambia la pantalla
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    //Llama el navBackStackEntry y dice exactamente el composable en el que esta, no muestra otros argumentos
    val currentDestination = navBackStackEntry?.destination

    //Remember hace que calcule una vez  el destination,
    // hierarchy chequea todas las rutas existentes, si el destination
    //esta en la ruta Home o Diary, que actualize la variable a true, y asi se muestra el bottombar
    // Calcula si la BottomBar debe mostrarse segun la pantalla actual.
    val showBottomBar = remember(currentDestination) {
        currentDestination?.hierarchy?.any {
            it.hasRoute(Home::class) || it.hasRoute(Diary::class)
        } == true //== true hace que acepte nulos como verdaderos, ya que puede haber nulos con el ?
    }

    //Se inicializa un viewmodel meals.
    // ViewModel compartido por pantallas relacionadas con comidas y diario.
    val mealsViewModel: MealsViewModel = viewModel()
    // ViewModel compartido por pantallas relacionadas con ejercicios.
    val workoutsViewModel: WorkoutViewModel = viewModel()


    val snackbarHostState = remember {
        SnackbarHostState()
    }

    // Escucha eventos del ViewModel para mostrar mensajes sin acoplarlos a una pantalla concreta.
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
                        navController.navigate(Register)
                    }
                )
            }

            //Pantalla de Registro

            composable<Register> {
                RegisterScreen(
                    onRegisterSuccess = { name ->
                        navController.navigate(Home(name = name)) {
                            popUpTo(Welcome) {
                                inclusive = true
                            }
                        }
                    },
                    onNavigateToLogin = {
                        navController.navigate(Login) {
                            popUpTo(Register) {
                                inclusive = true
                            }
                        }
                    }
                )
            }
            //Pantalla de Login

            composable<Login> {
                val authViewModel: AuthViewModel = viewModel()

                // Escucha eventos del AuthViewModel para navegar al Home o mostrar errores.
                LaunchedEffect(Unit) {
                    authViewModel.events.collect { event ->
                        when(event) {
                            is AuthEvent.Success ->
                                navController.navigate(Home(name = event.name)) {
                                    popUpTo(Welcome) {inclusive = true}
                                }
                            is AuthEvent.ShowError -> snackbarHostState.showSnackbar(event.message)
                        }
                    }
                }
                LoginScreen(viewModel = authViewModel)
            }

            //Pantalla de Home
            composable<Home> {
                HomeScreen(
                    onCalorieCardClick = {navController.navigate(Diary(""))},
                    onWorkoutCardClick = {navController.navigate(WorkoutCatalog)}
                )
            }

            composable<Diary> {
                DiaryScreen(
                    viewModel = mealsViewModel,
                    onAddMealClick = {navController.navigate(Catalog)},
                    onMealEditClick = {id -> navController.navigate(EditMeal(id))}
                )
            }

            composable<Catalog> {
                CatalogScreen(
                    viewModel = mealsViewModel,
                    onMealClick = { id ->
                        navController.navigate(AddMeal(id))
                    },
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }

            composable<AddMeal> { entry ->
                val args = entry.toRoute<AddMeal>()
                AddMeal(
                    viewModel = mealsViewModel,
                    mealId = args.mealId,
                    onMealAdd = {
                        navController.popBackStack(Catalog, inclusive = true)
                    },
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }

            //Pantalla de Edit
            composable<EditMeal> {
                entry -> val args = entry.toRoute<EditMeal>()
                EditMealScreen(
                    vm = mealsViewModel,
                    entryId = args.mealId,
                    onCompleted = {navController.popBackStack()},
                    onBack = {navController.popBackStack()}
                )
            }

            //Workouts
            
            composable<WorkoutCatalog> {
                WorkoutCatalogScreen(
                    viewModel = workoutsViewModel,
                    onWorkoutClick = {
                        TODO()
                    },
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}