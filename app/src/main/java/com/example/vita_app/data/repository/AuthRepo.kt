package com.example.vita_app.data.repository

import com.example.vita_app.data.RetrofitHelper
import com.example.vita_app.data.remote.api.AuthApi
import com.example.vita_app.data.remote.model.ApiError
import com.example.vita_app.data.remote.model.LoginRequest
import com.example.vita_app.data.remote.model.RegisterRequest
import com.google.gson.Gson
import retrofit2.Response


sealed class AuthResult {
    data class Success(val data: String) : AuthResult()
    data class Error(val message: String) : AuthResult()
}
class AuthRepo {
    private val api = RetrofitHelper.getInstance().create(AuthApi::class.java)

    //Mensaje de error segun codigo HTTP.

    private fun parseError(res: Response<*>): String {
        return when (res.code()) {
            400 -> "Datos invalidos"
            401 -> "Correo o contraseña incorrectos"
            409 -> "Ese correo ya esta registrado"
            in 500..599 -> "Error del servidor, intenta mas tarde"
            else -> "Algo salio mal (${res.code()})"
        }
    }

    suspend fun loginUser(email: String, password: String): AuthResult {
        try {
            val res = api.login(LoginRequest(email, password))
            if (res.isSuccessful)
                return AuthResult.Success(res.body()?.token ?: return AuthResult.Error("Empty token") )
            else
                return AuthResult.Error(parseError(res))
        } catch (ex: Exception) {
            return AuthResult.Error(ex.message?: "Error")
        }
    }

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