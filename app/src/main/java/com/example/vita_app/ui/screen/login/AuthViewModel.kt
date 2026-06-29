package com.example.vita_app.ui.screen.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vita_app.data.TokenManager
import com.example.vita_app.data.repository.AuthRepo
import com.example.vita_app.data.repository.AuthResult
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

sealed class AuthEvent {
    data class Success(val name: String): AuthEvent()

    data class ShowError(val message: String) : AuthEvent()
}

class AuthViewModel : ViewModel() {
    private val repo = AuthRepo()

    var isLoading by mutableStateOf(false)
        private set

    private val _events = Channel<AuthEvent>()
    val events = _events.receiveAsFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            isLoading = true

            when(val result = repo.loginUser(email, password)) {
                is AuthResult.Success -> {
                    TokenManager.token = result.data
                    _events.send(AuthEvent.Success(email))
                }

                is AuthResult.Error -> _events.send(AuthEvent.ShowError(result.message))
            }

            isLoading = false
        }
    }
}