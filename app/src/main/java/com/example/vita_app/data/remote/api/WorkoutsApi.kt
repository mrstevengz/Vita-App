package com.example.vita_app.data.remote.api

import com.example.vita_app.data.remote.model.Workout
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface WorkoutsApi {
    @GET("workouts")
    suspend fun getWorkouts() : List<Workout>

    @GET("workouts/{id}")
    suspend fun getWorkoutsById(
        @Path("id") id: Int
    ): Workout

    //POST para crear nuevo Meal
    @POST("workouts")
    suspend fun createWorkouts(
        @Body workout: Workout
    ): Workout

    //Put para actualizar Meal existente
    @PUT("workouts/{id}")
    suspend fun updateWorkouts(
        @Path("id") id: Int,
        @Body workout: Workout
    ) : Workout

    //Delete

    @DELETE("workouts/{id}")
    suspend fun deleteWorkouts(
        @Path("id") id: Int
    )
}