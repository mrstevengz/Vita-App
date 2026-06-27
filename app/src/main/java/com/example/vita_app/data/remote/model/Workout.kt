package com.example.vita_app.data.remote.model

import com.google.gson.annotations.SerializedName

data class WorkoutResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("caloriesPerHour") val caloriesPerHour: String,
    @SerializedName("description") val description: String,
)

data class WorkoutRequest(
    @SerializedName("name") val name: String,
    @SerializedName("caloriesPerHour") val caloriesPerHour: String,
    @SerializedName("description") val description: String

)