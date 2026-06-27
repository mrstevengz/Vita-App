package com.example.vita_app.data.remote.api

import com.example.vita_app.data.remote.model.DiaryEntryRequest
import com.example.vita_app.data.remote.model.DiaryEntryResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface EntriesApi {
    @GET("entries")
    suspend fun getEntries(): List<DiaryEntryResponse>

    @POST("entries")
    suspend fun createEntry(
        @Body body: DiaryEntryRequest
    ): DiaryEntryResponse

    @PUT("entries/{id}")
    suspend fun updateEntry(
        @Path("id") id: Int,
        @Body body: DiaryEntryRequest
    ): DiaryEntryResponse

    @DELETE("entries/{id}")
    suspend fun deleteEntry(
        @Path("id") id: Int
    ): Response<Unit>
}