package com.example.vita_app.data

// Proposito: Guarda en memoria el token JWT que devuelve el login para usarlo en endpoints protegidos.

// Objeto global sencillo para compartir el token entre repositorios protegidos.
object TokenManager {
    var token: String? = null
}