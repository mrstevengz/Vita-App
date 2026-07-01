package com.example.vita_app.data
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
// Singleton para crear una sola instancia de Retrofit durante la ejecucion de la app.
object RetrofitHelper {
    // Proposito: Configura Retrofit, que es el cliente HTTP usado para comunicarse con la API local.
    private val baseUrl = "https://vita-app-api.onrender.com/api/"


    private var instance: Retrofit? = null
    // Devuelve la instancia existente o la construye la primera vez que se necesita.
    fun getInstance(): Retrofit {
        if (instance == null) {
            instance = Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create()).build()
        }
        return instance!!
    }

}