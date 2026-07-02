package com.example.vita_app.data.remote.model

import com.google.gson.annotations.SerializedName

// Proposito: Modelos de datos para registrar comidas en el diario diario del usuario.

// Enum que coincide con las secciones aceptadas por la API para el diario.
enum class MealType {
    BREAKFAST, LUNCH, DINNER, SNACKS
}
// Entrada completa del diario recibida desde la API, incluyendo la comida relacionada.
data class DiaryEntryResponse (
    @SerializedName("id") val id: Int,
    @SerializedName("userId") val userId: Int,
    @SerializedName("mealId") val mealId: Int,
    @SerializedName("meal") val meal: MealResponse,
    @SerializedName("grams") val grams: String,
    @SerializedName("section") val section: MealType,
    @SerializedName("date") val date: String
)

// Datos minimos que la app envia al crear o actualizar una entrada de comida.
data class DiaryEntryRequest(
    @SerializedName("mealId") val mealId: Int,
    @SerializedName("grams") val grams: String,
    @SerializedName("section") val section: MealType
)