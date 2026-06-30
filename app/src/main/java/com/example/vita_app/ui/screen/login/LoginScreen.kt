package com.example.vita_app.ui.screen.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import com.example.vita_app.R
import com.example.vita_app.ui.theme.CarbonBlack
import com.example.vita_app.ui.theme.LightCyan
import com.example.vita_app.ui.theme.PineBlue
import com.example.vita_app.ui.theme.SoftTurqoise
import com.example.vita_app.ui.theme.PastelCyan
import com.example.vita_app.ui.theme.White

@Composable
fun LoginScreen(viewModel: AuthViewModel) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Contenedor principal
    Box(modifier = Modifier.fillMaxSize()) {

        // HEADER fondio superior
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(320.dp)
                .background(
                    // Gradiente de colores
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
                // Capa oscura encima (para efecto visual)
                modifier = Modifier.matchParentSize()
                    .background(Color.Black.copy(alpha = 0.05f))
            )
        }

        Column(
            modifier = Modifier.fillMaxSize().padding(top = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(80.dp))

            //  LOGO
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = null,
                modifier = Modifier.size(150.dp)
            )

            Spacer(modifier = Modifier.height(80.dp))

            // CARD
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(horizontal = 24.dp),
                shape = RoundedCornerShape(28.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                colors = CardDefaults.cardColors(containerColor = White) // Color de fondo de la tarjeta
            ) {

                Column(
                    modifier = Modifier.padding(24.dp)
                ) {

                    Text(
                        "Welcome!",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = CarbonBlack
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },    // Guarda lo que el usuario escribe
                        placeholder = { Text("Email") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))


                    // Estado para mostrar u ocultar contraseña
                    var showPassword by remember { mutableStateOf(false) }

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        placeholder = { Text("Password") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        singleLine = true,
                        // Si showPassword es true → muestra texto
                        // si es false → oculta con puntos
                        visualTransformation = if (showPassword)
                            VisualTransformation.None
                        else
                            PasswordVisualTransformation(),
                        // Icono para mostrar/ocultar contraseña
                        trailingIcon = {
                            Row(
                                modifier = Modifier
                                    .clickable { showPassword = !showPassword }
                                    .padding(end = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = if (showPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = if (showPassword) "Ocultar" else "Mostrar",
                                    tint = PineBlue,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(2.dp))
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Checkbox(
                            checked = false, // no guarda estado aún
                            onCheckedChange = {},
                            colors = CheckboxDefaults.colors(
                                checkedColor = PineBlue
                            )
                        )

                        Text("Remember me", fontSize = 12.sp)

                        Spacer(modifier = Modifier.weight(1f))

                        Text(
                            "Forget password?",
                            fontSize = 12.sp,
                            color = PineBlue
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            // Solo permite continuar si hay username
                            if (email.isNotEmpty() && password.isNotEmpty()) {
                                viewModel.login(email, password) // Llama a la navegación (pantalla Home)
                            }
                        },
                        enabled = !viewModel.isLoading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(30.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PineBlue
                        )
                    ) {
                        if (viewModel.isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text("Login", color = White)
                        }
                    }
                }
            }
            // Empuja tdo a arriba
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}