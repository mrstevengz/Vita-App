package com.example.vita_app.data
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {
    private val baseUrl = "https://vita-app-api.onrender.com/api/" //Hardcoded por ahora, tiene que ser una variable env

    private var instance: Retrofit? = null

    fun getInstance(): Retrofit {
        if (instance == null) {
            instance = Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create()).build()
        }
        return instance!!
    }

}