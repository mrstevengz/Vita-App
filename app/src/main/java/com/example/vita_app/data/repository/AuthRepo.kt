package com.example.vita_app.data.repository

// Proposito: Repositorio de autenticacion. Encapsula la llamada a la API y guarda el token recibido.

import com.example.vita_app.data.RetrofitHelper
import com.example.vita_app.data.remote.api.AuthApi
import com.example.vita_app.data.remote.model.LoginRequest


// Resultado controlado para comunicar exito o error de login al ViewModel.
sealed class AuthResult {
    data class Success(val data: String) : AuthResult()
    data class Error(val message: String) : AuthResult()
}
// Capa de datos para autenticacion; evita que la UI conozca detalles de Retrofit.
class AuthRepo {
    private val api = RetrofitHelper.getInstance().create(AuthApi::class.java)

    // Ejecuta el login en background, guarda token si existe y devuelve un resultado para la UI.
    suspend fun loginUser(email: String, password: String): AuthResult {
        try {
            val res = api.login(LoginRequest(email, password))
            if (res.isSuccessful)
                return AuthResult.Success(res.body()?.token ?: return AuthResult.Error("Empty token") )
            else
                return AuthResult.Error(res.errorBody()?.string() ?: "500: Error")
        } catch (ex: Exception) {
            return AuthResult.Error(ex.message?: "Error")
        }
    }
}