package com.example.vita_app.data.remote.model

import com.google.gson.annotations.SerializedName

data class Workout(
    @SerializedName("id")             val id: Int,

    @SerializedName("name")           val name: String,

    @SerializedName("caloriesPerHour")        val minutes: Double,
)