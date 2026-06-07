package com.example.vita_app.ui.navigation

import kotlinx.serialization.Serializable


//RUTAS DE LA APP. CADA DATA CLASS/OBJETO ES UNA PANTALLA
//Se usan object para pantallas sin parametros, y dataclass para pantallas con ids, nombres, etc
@Serializable
object Welcome

@Serializable
object Login

@Serializable
data class Home(val name : String)
@Serializable
data class Diary(val name : String)

@Serializable
object AddMeal