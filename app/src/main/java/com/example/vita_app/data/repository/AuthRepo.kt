package com.example.vita_app.data.repository

import com.example.vita_app.data.RetrofitHelper
import com.example.vita_app.data.remote.api.AuthApi
import com.example.vita_app.data.remote.model.ApiError
import com.example.vita_app.data.remote.model.LoginRequest
import com.example.vita_app.data.remote.model.RegisterRequest
import com.google.gson.Gson
import retrofit2.Response

// Proposito: Repositorio de autenticacion. Encapsula la llamada a la API y guarda el token recibido.

// Resultado controlado para comunicar exito o error de login al ViewModel.
sealed class AuthResult {
    data class Success(val data: String) : AuthResult() //Lleva el token
    data class Error(val message: String) : AuthResult() //Lleva el mensaje de error
}

// Capa de datos para autenticacion; evita que la UI conozca detalles de Retrofit.
class AuthRepo {
    private val api = RetrofitHelper.getInstance().create(AuthApi::class.java)

    //Mensaje de error segun codigo HTTP.

    private fun parseError(res: Response<*>): String {
        //When funciona como un Switch
        return when (res.code()) {
            400 -> "Datos invalidos"
            401 -> "Correo o contraseña incorrectos"
            409 -> "Ese correo ya esta registrado"
            in 500..599 -> "Error del servidor, intenta mas tarde"
            else -> "Algo salio mal (${res.code()})"
        }
    }

    // Ejecuta el login en background, guarda token si existe y devuelve un resultado para la UI.
    suspend fun loginUser(email: String, password: String): AuthResult {
        try {
            val res = api.login(LoginRequest(email, password))
            if (res.isSuccessful)
                //?: Elvis operator, si el token es nullo, retorna el error en vez de crashear
                return AuthResult.Success(res.body()?.token ?: return AuthResult.Error("Empty token") )
            else
                return AuthResult.Error(parseError(res))
        } catch (ex: Exception) {
            return AuthResult.Error(ex.message?: "Error")
        }
    }

    //Ejecuta el register en background, retorna success y si no manda mensaje de error
    suspend fun registerUser(email: String, password: String): AuthResult {
        try {
            val res = api.register(RegisterRequest(email, password))
            return if (res.isSuccessful)
                AuthResult.Success("")
            else
                AuthResult.Error(parseError(res))
        }catch (ex: Exception) {
            return AuthResult.Error(ex.message ?: "Error")
        }
    }
}