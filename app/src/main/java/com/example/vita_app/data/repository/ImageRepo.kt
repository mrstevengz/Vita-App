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

    //Forma del bearer token authorization
    private fun bearer() = "Bearer ${TokenManager.token}"


    //Recibe ImageBytes (la foto en crudo, solo bytes)
    suspend fun analyze(imageBytes: ByteArray): AnalyzeResponse {
        //Se guardan los bytes en un requestbody (Okhttp) que hace viaja en la peticion.
        //Se le pasan dos cosas, un media type (etiqueta que dice esto es un JPEG)
        // y imageBytes, los datos reales de la fotoen bytes
        val body = RequestBody.create(MediaType.parse("image/jpeg"), imageBytes)

        //Se mete ese body adentro de un formulario multipart,
        //createFormData recibe tres argumentos, el nombre del campo, el nombre del archivo y el contenido
        val part = MultipartBody.Part.createFormData("image", "meal.jpg", body)

        //Se manda a llamar al endpoint del API, se le pasa el token y el form-data
        return api.analyze(bearer(), part)
    }
}