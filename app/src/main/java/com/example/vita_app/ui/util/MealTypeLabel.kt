package com.example.vita_app.ui.util

import com.example.vita_app.data.remote.model.MealType

fun MealType.label(): String = when(this){
    MealType.BREAKFAST -> "Desayuno"
    MealType.LUNCH     -> "Almuerzo"
    MealType.DINNER    -> "Cena"
    MealType.SNACKS    -> "Snacks"
}