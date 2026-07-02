package com.example.vita_app.data.remote.api

import com.example.vita_app.data.remote.model.AnalyzeResponse
import okhttp3.MultipartBody
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ImageApi {
    @Multipart
    @POST("analyze")
    suspend fun analyze(
        @Header("Authorization") token: String,
        @Part image: MultipartBody.Part
    ): AnalyzeResponse
}