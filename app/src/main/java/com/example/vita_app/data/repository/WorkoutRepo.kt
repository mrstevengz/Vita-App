package com.example.vita_app.data.repository

import com.example.vita_app.data.RetrofitHelper
import com.example.vita_app.data.remote.api.WorkoutsApi
import com.example.vita_app.data.remote.model.WorkoutResponse

class WorkoutRepo {
    private val api = RetrofitHelper.getInstance().create(WorkoutsApi::class.java)

    suspend fun getWorkouts(): List<WorkoutResponse> {
        return api.getWorkouts()
    }
}