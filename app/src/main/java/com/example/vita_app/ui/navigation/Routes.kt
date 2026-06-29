package com.example.vita_app.ui.navigation

// Proposito: Define las rutas tipadas que usa Navigation Compose para moverse entre pantallas.

import kotlinx.serialization.Serializable


//RUTAS DE LA APP. CADA DATA CLASS/OBJETO ES UNA PANTALLA
//Se usan object para pantallas sin parametros, y dataclass para pantallas con ids, nombres, etc
@Serializable
// Ruta de la pantalla inicial.
object Welcome

@Serializable
// Ruta de registro de usuario.
object Register
@Serializable
// Ruta de inicio de sesion.
object Login
@Serializable
// Ruta de Home; recibe el nombre para personalizar el saludo.
data class Home(val name : String)

@Serializable
// Ruta del diario de comidas.
data class Diary(val name : String)

@Serializable
// Ruta del catalogo de comidas.
object Catalog
@Serializable
// Ruta para agregar una comida; transporta el id de la comida elegida.
data class AddMeal(val mealId: Int)
@Serializable
// Ruta para editar una entrada existente del diario.
data class EditMeal(val mealId: Int)

//Workouts

@Serializable
// Ruta del catalogo de ejercicios.
object WorkoutCatalog