package com.example.vita_app.data.remote.api

// Proposito: Define las llamadas HTTP para consultar el catalogo de comidas de la API.

import com.example.vita_app.data.remote.model.MealResponse
import retrofit2.http.GET


// Interfaz Retrofit para endpoints publicos del catalogo de comidas.
interface MealsApi {
    //Getter para todos y un ID
    @GET("meals")
    suspend fun getMeals() : List<MealResponse>
}