package com.example.vita_app.data.remote.model

import com.google.gson.annotations.SerializedName

data class MealEntry (
    @SerializedName("id") val id: Int? = null,
    @SerializedName("mealId") val mealId: Int? = null,
    @SerializedName("grams") val double: Double = 0.0,
    @SerializedName("section") val section: Section
)

enum class Section {
    BREAKFAST, LUNCH, DINNER, SNACKS
}