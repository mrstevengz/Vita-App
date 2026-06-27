package com.example.vita_app.data.remote.model

import com.google.gson.annotations.SerializedName

data class WorkoutEntry (
    @SerializedName("id") val id: Int? = null,
    @SerializedName("workoutId") val workoutId: Int,
    @SerializedName("minutes") val minutes: Double
)
