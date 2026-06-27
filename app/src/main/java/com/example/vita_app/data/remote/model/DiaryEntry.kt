package com.example.vita_app.data.remote.model

import com.google.gson.annotations.SerializedName

enum class MealType {
    BREAKFAST, LUNCH, DINNER, SNACKS
}
data class DiaryEntryResponse (
    @SerializedName("id") val id: Int,
    @SerializedName("userId") val userId: Int,
    @SerializedName("mealId") val mealId: Int,
    @SerializedName("meal") val meal: MealResponse,
    @SerializedName("grams") val grams: String,
    @SerializedName("section") val section: MealType,
    @SerializedName("date") val date: String
)

data class DiaryEntryRequest(
    @SerializedName("mealId") val mealId: Int,
    @SerializedName("grams") val grams: String,
    @SerializedName("section") val section: MealType
)