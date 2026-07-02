package com.example.vita_app.data

// Proposito: Guarda en memoria el token JWT que devuelve el login para usarlo en endpoints protegidos.

// Objeto global sencillo para compartir el token entre repositorios protegidos.
object TokenManager {
    //Guarda el token en memoria como un singleton (una instancia en toda la app), puede ser null = "no hay sesion o token"
    var token: String? = null
}