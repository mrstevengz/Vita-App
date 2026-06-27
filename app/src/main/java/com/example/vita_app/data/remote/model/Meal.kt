package com.example.vita_app.data.remote.model

import com.google.gson.annotations.SerializedName

data class MealResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("calories") val calories: String,
    @SerializedName("carbs") val carbs: String,
    @SerializedName("fat") val fat: String,
    @SerializedName("protein") val protein: String,
)

data class MealRequest(
    @SerializedName("name") val name: String,
    @SerializedName("calories") val calories: String,
    @SerializedName("carbs") val carbs: String,
    @SerializedName("fat") val fat: String,
    @SerializedName("protein") val protein: String,
)