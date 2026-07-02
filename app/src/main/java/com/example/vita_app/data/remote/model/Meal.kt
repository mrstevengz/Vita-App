package com.example.vita_app.data.remote.model

import com.google.gson.annotations.SerializedName //Anotacion de GSON para mapear llaves JSON

// Proposito: Modelos de datos para comidas segun el formato que devuelve y recibe la API.

// Representa una comida recibida desde GET /meals.
data class MealResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("calories") val calories: String, //Calorias y otros datos numericos se dejan como String, ya que Prisma.Decimal manda "150"
    @SerializedName("carbs") val carbs: String,
    @SerializedName("fat") val fat: String,
    @SerializedName("protein") val protein: String,
)

// Representa una comida que se podria enviar a la API para crear o actualizar, sin ID, aun no existe
data class MealRequest(
    @SerializedName("name") val name: String,
    @SerializedName("calories") val calories: String,
    @SerializedName("carbs") val carbs: String,
    @SerializedName("fat") val fat: String,
    @SerializedName("protein") val protein: String,
)