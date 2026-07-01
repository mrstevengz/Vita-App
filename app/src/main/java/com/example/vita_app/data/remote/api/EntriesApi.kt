package com.example.vita_app.data.remote.api

import com.example.vita_app.data.remote.model.DiaryEntryRequest
import com.example.vita_app.data.remote.model.DiaryEntryResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

// Proposito: Define las llamadas HTTP para leer y modificar las comidas registradas en el diario del usuario.
// Interfaz Retrofit para endpoints que requieren Authorization Bearer token.
interface EntriesApi {
    @GET("entries")
    suspend fun getEntries(
        @Header("Authorization") token: String
    ): List<DiaryEntryResponse>

    @POST("entries")
    suspend fun createEntry(
        @Body body: DiaryEntryRequest,
        @Header("Authorization") token: String
    ): DiaryEntryResponse

    @PUT("entries/{id}")
    suspend fun updateEntry(
        @Path("id") id: Int,
        @Body body: DiaryEntryRequest,
        @Header("Authorization") token: String
    ): DiaryEntryResponse

    @DELETE("entries/{id}")
    suspend fun deleteEntry(
        @Path("id") id: Int,
        @Header("Authorization") token: String
    ): Response<Unit>
}