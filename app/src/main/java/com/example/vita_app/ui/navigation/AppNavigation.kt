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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
import com.example.vita_app.data.TokenManager
import com.example.vita_app.data.TokenStore
import com.example.vita_app.ui.components.BottomBar
import com.example.vita_app.ui.screen.meals.AddMeal
import com.example.vita_app.ui.screen.meals.CatalogScreen
import com.example.vita_app.ui.screen.diary.DiaryScreen
import com.example.vita_app.ui.screen.meals.EditMealScreen
import com.example.vita_app.ui.screen.login.AuthEvent
import com.example.vita_app.ui.screen.login.AuthViewModel
import com.example.vita_app.ui.screen.login.LoadingScreen
import com.example.vita_app.ui.screen.login.RegisterScreen
import com.example.vita_app.ui.screen.meals.MealsViewModel
import com.example.vita_app.ui.screen.workouts.AddWorkoutScreen
import com.example.vita_app.ui.screen.workouts.EditWorkoutScreen
import com.example.vita_app.ui.screen.workouts.WorkoutCatalogScreen
import com.example.vita_app.ui.screen.workouts.WorkoutViewModel
import kotlinx.coroutines.launch


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
    val workoutsViewModel: WorkoutViewModel = viewModel()


    val snackbarHostState = remember {
        SnackbarHostState()
    }

    LaunchedEffect(Unit) {
        mealsViewModel.events.collect { message -> snackbarHostState.showSnackbar(message) }
    }
    LaunchedEffect(Unit) {
        workoutsViewModel.events.collect {message -> snackbarHostState.showSnackbar(message)}
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
            startDestination = Splash,
            modifier = Modifier.fillMaxSize()
        ) {
            //Pantalla de carga
            composable<Splash> {
                val context = LocalContext.current
                LaunchedEffect(Unit) {
                    val tokenStore = TokenStore(context.applicationContext)
                    val saved = tokenStore.read()

                    TokenManager.token = saved

                    val destination = if (saved!=null) Home("")
                    else Welcome

                    navController.navigate(destination) {
                        popUpTo(Splash) {inclusive = true}
                    }
                }
                LoadingScreen()
            }
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
               val authViewModel: AuthViewModel = viewModel()

               LaunchedEffect(Unit) {
                   authViewModel.events.collect {
                       event -> when (event) {
                           is AuthEvent.RegisterSuccess ->
                               navController.navigate(Login) {
                                   popUpTo(Register) { inclusive = true}
                               }
                            is AuthEvent.ShowError -> {
                                snackbarHostState.showSnackbar(event.message)
                            }
                            is AuthEvent.Success -> {}
                       }
                   }
               }
               RegisterScreen(
                   viewModel = authViewModel,
                   onNavigateToLogin = {
                       navController.navigate(Login) {
                           popUpTo(Register) {inclusive = true}
                       }
                   }
               )
           }
            //Pantalla de Login

            composable<Login> {
                val authViewModel: AuthViewModel = viewModel()

                LaunchedEffect(Unit) {
                    authViewModel.events.collect { event ->
                        when(event) {
                            is AuthEvent.Success ->
                                navController.navigate(Home(name = event.name)) {
                                    popUpTo(Welcome) {inclusive = true}
                                }
                            is AuthEvent.ShowError -> snackbarHostState.showSnackbar(event.message)
                            is AuthEvent.RegisterSuccess -> {}
                        }
                    }
                }
                LoginScreen(viewModel = authViewModel)
            }

            //Pantalla de Home
            composable<Home> {
                val context = LocalContext.current
                val scope = rememberCoroutineScope()
                HomeScreen(
                    onCalorieCardClick = {navController.navigate(Diary(""))},
                    onWorkoutCardClick = {navController.navigate(WorkoutCatalog)},
                    mealsViewModel = mealsViewModel,
                    workoutViewModel = workoutsViewModel,
                    onLogout = {
                        scope.launch {
                            TokenManager.token = null
                            TokenStore(context.applicationContext).clear()
                            navController.navigate(Welcome) {
                                popUpTo(0) {inclusive = true}
                            }

                        }
                    }
                )
            }

            composable<Diary> {
                DiaryScreen(
                    viewModel = mealsViewModel,
                    workoutsViewModel = workoutsViewModel,
                    onAddMealClick = {navController.navigate(Catalog)},
                    onMealEditClick = {id -> navController.navigate(EditMeal(id))},
                    onAddWorkoutClick = {navController.navigate(WorkoutCatalog)},
                    onWorkoutEditClick = {id -> navController.navigate(EditWorkout(id))}
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
                        id -> navController.navigate(AddWorkout(id))
                    },
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }

            composable<AddWorkout> { entry ->
                val args = entry.toRoute<AddWorkout>()
                AddWorkoutScreen(
                    viewModel = workoutsViewModel,
                    workoutId = args.workoutId,
                    onAdd = {
                        navController.popBackStack(WorkoutCatalog, inclusive = true)
                    },
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }

            composable<EditWorkout> {entry ->
                val args = entry.toRoute<EditWorkout>()

                EditWorkoutScreen(
                    viewmodel = workoutsViewModel,
                    entryId = args.entryId,
                    onCompleted = {navController.popBackStack()},
                    onBack = {navController.popBackStack()}
                )
            }
        }
    }
}