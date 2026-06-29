package com.example.vita_app.data.repository

// Proposito: Repositorio de entradas de ejercicios. Agrega autenticacion y maneja errores de eliminacion.

import com.example.vita_app.data.RetrofitHelper
import com.example.vita_app.data.TokenManager
import com.example.vita_app.data.remote.api.WorkoutEntriesApi
import com.example.vita_app.data.remote.model.WorkoutEntryRequest
import com.example.vita_app.data.remote.model.WorkoutEntryResponse
import retrofit2.http.Body

// Capa de acceso a los ejercicios registrados por el usuario autenticado.
class WorkoutEntryRepo {
    private val api = RetrofitHelper.getInstance().create(WorkoutEntriesApi::class.java)

    private fun bearer(): String = "Bearer ${TokenManager.token}"

    //Funciones
    suspend fun getEntries(): List<WorkoutEntryResponse> = api.getEntries(bearer())

    suspend fun createWorkoutEntry(
        body: WorkoutEntryRequest
    ): WorkoutEntryResponse = api.createEntry(body, bearer())

    suspend fun updateWorkoutEntry(
        id: Int,
        body: WorkoutEntryRequest
    ): WorkoutEntryResponse = api.updateEntry(id, body, bearer())

    suspend fun deleteWorkoutEntry(id: Int) {
        val res = api.deleteEntry(id, bearer())
        if (!res.isSuccessful) {
            throw Exception("Fallo al eliminar: ${res.code()}")
        }
    }
}