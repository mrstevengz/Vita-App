package com.example.vita_app.ui.util

import com.example.vita_app.data.remote.model.MealType

//Se le agrega label al enum MealType
//Como MealType es un enum cerrado, se obliga a cubrir los 4 casos con when(this)
fun MealType.label(): String = when(this){
    MealType.BREAKFAST -> "Desayuno"
    MealType.LUNCH     -> "Almuerzo"
    MealType.DINNER    -> "Cena"
    MealType.SNACKS    -> "Snacks"
}