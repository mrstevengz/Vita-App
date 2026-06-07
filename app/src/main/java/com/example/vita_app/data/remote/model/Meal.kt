package com.example.vita_app.data.remote.model

import com.google.gson.annotations.SerializedName

data class Meal(
    @SerializedName("id")         val id: Int,

    @SerializedName("name")       val name: String,

    @SerializedName("calories")   val calories: Double,

    @SerializedName("fat")        val fat: Double,

    @SerializedName("protein")    val protein: Double,

    @SerializedName("directions") val directions: String
)