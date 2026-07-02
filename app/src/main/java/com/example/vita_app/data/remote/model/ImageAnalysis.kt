package com.example.vita_app.data.remote.model

import com.google.gson.annotations.SerializedName

//Respuesta del POST /analyze, results es una lista, ya que el JSON trae un array
//GSON lo mapea a List<DetectedFood>
data class AnalyzeResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("results") val results: List<DetectedFood>
)

//Cada comida que la IA detecto en la foto
data class DetectedFood(
    @SerializedName("detectedName") val detectedName: String, //lo que la IA vio
    @SerializedName("mealId") val mealId: Int?, //Null en caso que de no encuentre match
    @SerializedName("mealName") val mealName: String?,
    //Double ya que el numero lo genera Gemini y no Prisma
    @SerializedName("estimatedGrams") val estimatedGrams: Double

)
