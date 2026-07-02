package com.example.vita_app.data.remote.model

import com.google.gson.annotations.SerializedName

// Cuerpo JSON que se envia a POST /auth/login.
data class LoginRequest(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)
// Cuerpo JSON que se envia a POST /auth/register
data class RegisterRequest(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)

// Respuesta del login: contiene el JWT que luego se envia como Bearer token.
data class TokenResponse(
    @SerializedName("token") val token: String
)

//Forma del cuerpo de error en el backend. EJ. {"error": ""}, error puede que traiga una llave o no, asi que permitimos null
data class ApiError(
    @SerializedName("error") val error: String?
)