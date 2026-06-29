package com.example.vita_app.data.remote.api

import com.example.vita_app.data.remote.model.WorkoutEntryRequest
import com.example.vita_app.data.remote.model.WorkoutEntryResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface WorkoutEntriesApi {
    @GET("workouts-entries")
    suspend fun getEntries(
        @Header("Authorization") token: String
    ): List<WorkoutEntryResponse>

    @POST("workouts-entries")
    suspend fun createEntry(
        @Body body: WorkoutEntryRequest,
        @Header("Authorization") token: String
    ): WorkoutEntryResponse

    @PUT("workouts-entries/{id}")
    suspend fun updateEntry(
        @Path("id") id: Int,
        @Body body: WorkoutEntryRequest,
        @Header("Authorization") token: String
    ): WorkoutEntryResponse

    @DELETE("workouts-entries/{id}")
    suspend fun deleteEntry(
        @Path("id") id: Int,
        @Header("Authorization") token: String
    ): Response<Unit>
}