package com.example.vita_app.data.remote.api

// Proposito: Define las llamadas HTTP para consultar el catalogo de ejercicios de la API.


import com.example.vita_app.data.remote.model.WorkoutResponse
import retrofit2.http.GET


// Interfaz Retrofit para consultar workouts disponibles en la API.
interface WorkoutsApi {
    @GET("workouts")
    suspend fun getWorkouts() : List<WorkoutResponse>
}