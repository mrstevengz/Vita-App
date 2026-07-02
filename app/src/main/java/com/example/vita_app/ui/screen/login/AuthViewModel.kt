package com.example.vita_app.ui.screen.login

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vita_app.data.TokenManager
import com.example.vita_app.data.TokenStore
import com.example.vita_app.data.repository.AuthRepo
import com.example.vita_app.data.repository.AuthResult
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

//Eventos de tipo clase, se manda un objeto para saber QUE paso
//Sealed significa que AuthEvent solo puede ser uno de estos tres.
//Obliga al usar un when a cubrir todos los casos posibles
sealed class AuthEvent {
    data class Success(val name: String): AuthEvent()

    data class ShowError(val message: String) : AuthEvent()
    object RegisterSuccess : AuthEvent()
}

//Se usa AndroidViewModel en vez de viewmodel normal, ya que recibe el application que es un CONTEXT
//Se usa porque se accede al DataStore que require context para llegar al disco
class AuthViewModel(application : Application) : AndroidViewModel(application) {
    private val repo = AuthRepo() //Repositorio de Auth
    private val tokenStore = TokenStore(application) //Usa el context que trajo de androidViewModel

    //Estado observable (siempre esta viendo si hay cambios en IsLoading). Private set significa que se lee desde afuera, pero solo el VM puede escribirlo
    var isLoading by mutableStateOf(false)
        private set

    //Canal de eventos
    private val _events = Channel<AuthEvent>()
    val events = _events.receiveAsFlow()


    fun login(email: String, password: String, rememberMe: Boolean) {
        viewModelScope.launch {
            isLoading = true //Enciende el spinner

            when(val result = repo.loginUser(email, password)) {
                is AuthResult.Success -> {
                    TokenManager.token = result.data //Token se guarda en memoria
                    if(rememberMe) tokenStore.save(result.data)//Remember me tambien se guarda en disco
                    else tokenStore.clear() //Si no, no queda nada viejo y se despeja
                    _events.send(AuthEvent.Success(email))// Avisa a la UI
                }

                //Muestra snackbar de error
                is AuthResult.Error -> _events.send(AuthEvent.ShowError(result.message))
            }

            isLoading = false //Apaga el spinner
        }
    }

    fun register(email: String, password: String) {
        viewModelScope.launch {
            isLoading = true
            when (val result = repo.registerUser(email, password)) {
                is AuthResult.Success -> _events.send(AuthEvent.RegisterSuccess)
                is AuthResult.Error -> _events.send(AuthEvent.ShowError(result.message))
            }
            isLoading = false
        }
    }
}