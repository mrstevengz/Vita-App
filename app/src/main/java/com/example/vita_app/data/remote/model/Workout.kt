package com.example.vita_app.data.remote.model

import com.google.gson.annotations.SerializedName

// Proposito: Modelos de datos para ejercicios segun el formato que devuelve y recibe la API.

// Representa un ejercicio recibido desde GET /workouts.
data class WorkoutResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("caloriesPerHour") val caloriesPerHour: String,
    @SerializedName("description") val description: String,
)

// Representa un ejercicio que se podria enviar a la API para crear o actualizar.
data class WorkoutRequest(
    @SerializedName("name") val name: String,
    @SerializedName("caloriesPerHour") val caloriesPerHour: String,
    @SerializedName("description") val description: String

)