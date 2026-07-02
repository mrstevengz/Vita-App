package com.example.vita_app.data.repository

import com.example.vita_app.data.RetrofitHelper
import com.example.vita_app.data.remote.api.MealsApi
import com.example.vita_app.data.remote.model.MealResponse
// Proposito: Repositorio de comidas. Sirve como capa intermedia entre ViewModel y MealsApi.

// Capa de acceso al catalogo de comidas.
class MealRepo {
    private val api = RetrofitHelper.getInstance().create(MealsApi::class.java)

    // Solicita todas las comidas disponibles en la API.
    suspend fun getMeals(): List<MealResponse> {
        return api.getMeals()
    }
}