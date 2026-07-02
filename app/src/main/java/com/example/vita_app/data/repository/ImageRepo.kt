package com.example.vita_app.data.repository

import com.example.vita_app.data.RetrofitHelper
import com.example.vita_app.data.TokenManager
import com.example.vita_app.data.remote.api.ImageApi
import com.example.vita_app.data.remote.model.AnalyzeResponse
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody

class ImageRepo {
    private val api = RetrofitHelper.getInstance().create(ImageApi::class.java)

    private fun bearer() = "Bearer ${TokenManager.token}"

    suspend fun analyze(imageBytes: ByteArray): AnalyzeResponse {
        val body = RequestBody.create(MediaType.parse("image/jpeg"), imageBytes)
        val part = MultipartBody.Part.createFormData("image", "meal.jpg", body)

        return api.analyze(bearer(), part)
    }
}