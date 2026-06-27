package com.example.vita_app.data.remote.api

import com.example.vita_app.data.remote.model.DiaryEntryRequest
import com.example.vita_app.data.remote.model.DiaryEntryResponse
import com.example.vita_app.data.remote.model.WorkoutEntryRequest
import com.example.vita_app.data.remote.model.WorkoutEntryResponse
import com.example.vita_app.data.remote.model.WorkoutRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface WorkoutEntry {
    @GET("workout-entries")
    suspend fun getEntries(): List<WorkoutEntryResponse>

    @POST("workout-entries")
    suspend fun createEntry(
        @Body body: WorkoutEntryRequest
    ): WorkoutEntryResponse

    @PUT("workout-entries/{id}")
    suspend fun updateEntry(
        @Path("id") id: Int,
        @Body body: WorkoutEntryRequest
    ): WorkoutEntryResponse

    @DELETE("workout-entries/{id}")
    suspend fun deleteEntry(
        @Path("id") id: Int
    ): Response<Unit>
}