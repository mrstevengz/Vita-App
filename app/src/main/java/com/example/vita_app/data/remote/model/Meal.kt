package com.example.vita_app.data.remote.model

// Proposito: Modelos de datos para comidas segun el formato que devuelve y recibe la API.

import com.google.gson.annotations.SerializedName

// Representa una comida recibida desde GET /meals.
data class MealResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("calories") val calories: String,
    @SerializedName("carbs") val carbs: String,
    @SerializedName("fat") val fat: String,
    @SerializedName("protein") val protein: String,
)

// Representa una comida que se podria enviar a la API para crear o actualizar.
data class MealRequest(
    @SerializedName("name") val name: String,
    @SerializedName("calories") val calories: String,
    @SerializedName("carbs") val carbs: String,
    @SerializedName("fat") val fat: String,
    @SerializedName("protein") val protein: String,
)