package com.example.vita_app.data.remote.api


import com.example.vita_app.data.remote.model.WorkoutResponse
import retrofit2.http.GET


interface WorkoutsApi {
    @GET("workouts")
    suspend fun getWorkouts() : List<WorkoutResponse>
}