package com.example.vita_app.data.remote.api

import com.example.vita_app.data.remote.model.MealResponse
import retrofit2.http.GET


interface MealsApi {
    //Getter para todos y un ID
    @GET("meals")
    suspend fun getMeals() : List<MealResponse>
}