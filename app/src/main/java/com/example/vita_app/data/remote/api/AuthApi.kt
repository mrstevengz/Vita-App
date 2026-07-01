package com.example.vita_app.data.remote.api

import com.example.vita_app.data.remote.model.LoginRequest
import com.example.vita_app.data.remote.model.RegisterRequest
import com.example.vita_app.data.remote.model.TokenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

// Proposito: Define las rutas de autenticacion de la API: login y registro.
interface AuthApi {

    @POST("auth/login")
    suspend fun login(@Body body: LoginRequest): Response<TokenResponse>
    @POST("auth/register")
    suspend fun register(@Body body: RegisterRequest): Response<Unit>
}