package com.example.vita_app.data.remote.model

// Proposito: Modelos de datos para registrar ejercicios realizados por el usuario.

import com.google.gson.annotations.SerializedName

// Entrada completa de ejercicio recibida desde la API, incluyendo el workout relacionado.
data class WorkoutEntryResponse (
    @SerializedName("id") val id: Int,
    @SerializedName("userId") val userId: Int,
    @SerializedName("workoutId") val workoutId: Int,
    @SerializedName("workout") val workout: WorkoutResponse,
    @SerializedName("minutes") val minutes: String,
    @SerializedName("date") val date: String
)

// Datos minimos que la app envia al registrar minutos de un workout.
data class WorkoutEntryRequest(
    @SerializedName("workoutId") val workoutId: Int,
    @SerializedName("minutes") val minutes: String,

)