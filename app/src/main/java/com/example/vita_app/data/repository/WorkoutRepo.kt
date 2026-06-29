package com.example.vita_app.data.repository

// Proposito: Repositorio de ejercicios. Sirve como capa intermedia entre ViewModel y WorkoutsApi.

import com.example.vita_app.data.RetrofitHelper
import com.example.vita_app.data.remote.api.WorkoutsApi
import com.example.vita_app.data.remote.model.WorkoutResponse

// Capa de acceso al catalogo de ejercicios.
class WorkoutRepo {
    private val api = RetrofitHelper.getInstance().create(WorkoutsApi::class.java)

    suspend fun getWorkouts(): List<WorkoutResponse> {
        return api.getWorkouts()
    }
}