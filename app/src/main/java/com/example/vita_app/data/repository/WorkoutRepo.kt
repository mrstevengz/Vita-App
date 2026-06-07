package com.example.vita_app.data.repository

import com.example.vita_app.data.RetrofitHelper
import com.example.vita_app.data.remote.api.WorkoutsApi
import com.example.vita_app.data.remote.model.Workout

class WorkoutRepo {
    private val api = RetrofitHelper.getInstance().create(WorkoutsApi::class.java)

    suspend fun getWorkouts(): List<Workout> {
        return api.getWorkouts()
    }

    suspend fun getMealById(id: Int): Workout{
        return api.getWorkoutsById(id)
    }

    suspend fun createWorkout(meal : Workout) : Workout {
        return api.createWorkouts(meal)
    }

    suspend fun updateWorkout(id: Int, workout: Workout): Workout {
        return api.updateWorkouts(id, workout)
    }

    suspend fun deleteWorkout(id: Int) {
        return api.deleteWorkouts(id)
    }
}