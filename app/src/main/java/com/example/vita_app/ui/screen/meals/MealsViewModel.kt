package com.example.vita_app.ui.screen.meals

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vita_app.data.remote.model.Meal
import com.example.vita_app.data.remote.model.MealType
import com.example.vita_app.data.repository.MealRepo
import kotlinx.coroutines.launch

class MealsViewModel: ViewModel() {
    private val repo = MealRepo()

    val meals = mutableStateListOf<Meal>()

    init {
        loadMeals()
    }

    fun loadMeals() {
        viewModelScope.launch {
            try {
                val mealsApi = repo.getMeals()
                meals.clear()
                meals.addAll(mealsApi)
            } catch (e: Exception) {
                println("Fallo al cargar los meals ${e.message}")
            }
        }
    }

    fun addMeal(name: String, calories: Double, section: MealType) {
        viewModelScope.launch {
            try {
                val meal = repo.createMeal(
                    Meal(name = name, calories = calories, section = section)
                )
                meals.add(meal)
                loadMeals()
            } catch(e: Exception) {
                println("Fallo al agregar meal: ${e.message}")
            }
        }
    }
}