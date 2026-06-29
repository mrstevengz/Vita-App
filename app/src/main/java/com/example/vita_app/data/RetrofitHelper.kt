package com.example.vita_app.data

// Proposito: Configura Retrofit, que es el cliente HTTP usado para comunicarse con la API local.
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Singleton para crear una sola instancia de Retrofit durante la ejecucion de la app.
object RetrofitHelper {
    private val baseUrl = "http://10.0.2.2:5000/api/" //Hardcoded por ahora, tiene que ser una variable env

    private var instance: Retrofit? = null

    // Devuelve la instancia existente o la construye la primera vez que se necesita.
    fun getInstance(): Retrofit {
        if (instance == null) {
            instance = Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create()).build()
        }
        return instance!!
    }

}