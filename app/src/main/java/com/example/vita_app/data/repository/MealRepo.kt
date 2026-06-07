package com.example.vita_app.data.repository

import com.example.vita_app.data.RetrofitHelper
import com.example.vita_app.data.remote.api.MealsApi
import com.example.vita_app.data.remote.model.Meal

class MealRepo {
    private val api = RetrofitHelper.getInstance().create(MealsApi::class.java)

    suspend fun getMeals(): List<Meal> {
        return api.getMeals()
    }

    suspend fun getMealById(id: Int): Meal {
        return api.getMealsById(id)
    }

    suspend fun createMeal(meal : Meal) : Meal {
        return api.createMeal(meal)
    }

    suspend fun updateMeal(id: Int, meal: Meal): Meal {
        return api.updateMeal(id, meal)
    }

    suspend fun deleteMeal(id: Int) {
        return api.deleteMeal(id)
    }
}