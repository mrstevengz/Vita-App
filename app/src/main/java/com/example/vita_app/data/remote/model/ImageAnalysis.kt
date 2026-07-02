package com.example.vita_app.data.remote.model

import com.google.gson.annotations.SerializedName

data class AnalyzeResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("results") val results: List<DetectedFood>
)

data class DetectedFood(
    @SerializedName("detectedName") val detectedName: String,
    @SerializedName("mealId") val mealId: Int?,
    @SerializedName("mealName") val mealName: String?,
    @SerializedName("estimatedGrams") val estimatedGrams: Double
)
