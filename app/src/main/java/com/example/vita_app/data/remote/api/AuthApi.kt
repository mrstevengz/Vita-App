package com.example.vita_app.data.remote.api

import com.example.vita_app.data.remote.model.LoginRequest
import com.example.vita_app.data.remote.model.RegisterRequest
import com.example.vita_app.data.remote.model.TokenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

// Proposito: Define las rutas de autenticacion de la API: login y registro.
interface AuthApi {
    //Con @Body, se manda el POST con email y password, y la respuesta es Response<Token> para
    //envolver el status + el token que se envia al responder

    @POST("auth/login")
    suspend fun login(@Body body: LoginRequest): Response<TokenResponse>

    //Como la respuesta solo es status, es Response<Unit> sin cuerpo
    @POST("auth/register")
    suspend fun register(@Body body: RegisterRequest): Response<Unit>
}