package com.example.vita_app.data
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
// Singleton para crear una sola instancia de Retrofit durante la ejecucion de la app.
object RetrofitHelper {
    // Proposito: Configura Retrofit, que es el cliente HTTP usado para comunicarse con la API local.
    private val baseUrl = "https://vita-app-api.onrender.com/api/"


    //Se guarda la instancia de retrofit en la variable, ? significa que puede ser null (nullable)
    private var instance: Retrofit? = null
    // Devuelve la instancia existente o la construye la primera vez que se necesita.
    fun getInstance(): Retrofit {
        if (instance == null) {
            //Sintaxis comun para retrofit, builder lo construye paso a paso, baseURl le da el API, se convierte de JSON a GSON, y arma el objeto con Build
            instance = Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create()).build()
        }
        //Se usa !! para decirle que NO puede ser null (se confirma con el IF)
        return instance!!
    }

}