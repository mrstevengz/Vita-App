package com.example.vita_app.ui.screen.login

// Proposito: Pantalla de registro de usuario con campos de entrada y validacion visual basica.

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vita_app.R
import com.example.vita_app.ui.theme.CarbonBlack
import com.example.vita_app.ui.theme.LightCyan
import com.example.vita_app.ui.theme.PastelCyan
import com.example.vita_app.ui.theme.PineBlue
import com.example.vita_app.ui.theme.SoftTurqoise
import com.example.vita_app.ui.theme.White

@Composable
// Formulario de registro; captura los datos necesarios para crear una cuenta.
fun RegisterScreen(
    onRegisterSuccess: (String) -> Unit,
    onNavigateToLogin: () -> Unit
) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var country by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var showConfirmPassword by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            LightCyan,
                            PastelCyan,
                            SoftTurqoise
                        )
                    )
                )
        ) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(Color.Black.copy(alpha = 0.05f))
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .imePadding()
                .padding(top = 32.dp, bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = null,
                modifier = Modifier.size(120.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(horizontal = 24.dp),
                shape = RoundedCornerShape(28.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                colors = CardDefaults.cardColors(containerColor = White)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        "Crear cuenta",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = CarbonBlack
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedTextField(
                            value = firstName,
                            onValueChange = { firstName = it },
                            placeholder = { Text("Nombres") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(20.dp),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = lastName,
                            onValueChange = { lastName = it },
                            placeholder = { Text("Apellidos") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(20.dp),
                            singleLine = true
                        )
                    }

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        placeholder = { Text("Correo electronico") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                    )

                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        placeholder = { Text("Numero telefonico") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                    )

                    OutlinedTextField(
                        value = country,
                        onValueChange = { country = it },
                        placeholder = { Text("Pais") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        singleLine = true
                    )

                    PasswordField(
                        value = password,
                        onValueChange = { password = it },
                        placeholder = "Contrasena",
                        showPassword = showPassword,
                        onTogglePassword = { showPassword = !showPassword }
                    )

                    PasswordField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        placeholder = "Confirmar contrasena",
                        showPassword = showConfirmPassword,
                        onTogglePassword = { showConfirmPassword = !showConfirmPassword }
                    )

                    errorMessage?.let {
                        Text(
                            text = it,
                            color = Color(0xFFB3261E),
                            fontSize = 12.sp
                        )
                    }

                    Button(
                        onClick = {
                            errorMessage = validateRegisterForm(
                                firstName = firstName,
                                lastName = lastName,
                                email = email,
                                phone = phone,
                                country = country,
                                password = password,
                                confirmPassword = confirmPassword
                            )

                            if (errorMessage == null) {
                                onRegisterSuccess(firstName.trim())
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(30.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PineBlue)
                    ) {
                        Text("Registrarse", color = White)
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Ya tienes cuenta?", fontSize = 12.sp)
                        TextButton(onClick = onNavigateToLogin) {
                            Text("Iniciar sesion", color = PineBlue)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    showPassword: Boolean,
    onTogglePassword: () -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation = if (showPassword) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        trailingIcon = {
            Row(
                modifier = Modifier
                    .clickable { onTogglePassword() }
                    .padding(end = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = if (showPassword) {
                        Icons.Default.VisibilityOff
                    } else {
                        Icons.Default.Visibility
                    },
                    contentDescription = if (showPassword) "Ocultar" else "Mostrar",
                    tint = PineBlue,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(2.dp))
            }
        }
    )
}

private fun validateRegisterForm(
    firstName: String,
    lastName: String,
    email: String,
    phone: String,
    country: String,
    password: String,
    confirmPassword: String
): String? {
    if (
        firstName.isBlank() ||
        lastName.isBlank() ||
        email.isBlank() ||
        phone.isBlank() ||
        country.isBlank() ||
        password.isBlank() ||
        confirmPassword.isBlank()
    ) {
        return "Completa todos los campos para crear tu cuenta."
    }

    if (!email.contains("@") || !email.contains(".")) {
        return "Ingresa un correo electronico valido."
    }

    if (password.length < 6) {
        return "La contrasena debe tener al menos 6 caracteres."
    }

    if (password != confirmPassword) {
        return "Las contrasenas no coinciden."
    }

    return null
}