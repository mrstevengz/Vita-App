package com.example.vita_app.data.remote.api

import com.example.vita_app.data.remote.model.Meal
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface MealsApi {
    //Getter para todos y un ID
    @GET("meals")
    suspend fun getMeals() : List<Meal>

    @GET("meals/{id}")
    suspend fun getMealsById(
        @Path("id") id: Int
    ): Meal

    //POST para crear nuevo Meal
    @POST("meals")
    suspend fun createMeal(
        @Body meal: Meal
    ): Meal

    //Put para actualizar Meal existente
    @PUT("meals/{id}")
    suspend fun updateMeal(
        @Path("id") id: Int,
        @Body meal: Meal
    ) : Meal

    //Delete

    @DELETE("meals/{id}")
    suspend fun deleteMeal(
        @Path("id") id: Int
    )
}