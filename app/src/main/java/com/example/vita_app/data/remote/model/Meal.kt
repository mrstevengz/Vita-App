package com.example.vita_app.data.remote.model

import com.google.gson.annotations.SerializedName

data class Meal(
    @SerializedName("id")         val id: Int? = null,

    @SerializedName("name")       val name: String,

    @SerializedName("calories")   val calories: Double,

    @SerializedName("fat")        val fat: Double = 0.0,

    @SerializedName("protein")    val protein: Double = 0.0,

    @SerializedName("directions") val directions: String = "",

    @SerializedName("section") val section: MealType
)