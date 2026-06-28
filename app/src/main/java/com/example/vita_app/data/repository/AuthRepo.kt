package com.example.vita_app.data.repository

import com.example.vita_app.data.RetrofitHelper
import com.example.vita_app.data.remote.api.AuthApi
import com.example.vita_app.data.remote.model.LoginRequest


sealed class AuthResult {
    data class Success(val data: String) : AuthResult()
    data class Error(val message: String) : AuthResult()
}
class AuthRepo {
    private val api = RetrofitHelper.getInstance().create(AuthApi::class.java)

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