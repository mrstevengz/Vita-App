package com.example.vita_app.ui.screen.login

// Proposito: Pantalla inicial que dirige al usuario hacia login o registro.


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vita_app.ui.theme.PineBlue
import com.example.vita_app.ui.theme.White

@Composable
// Primera pantalla visible; ofrece entrada a login o registro.
fun WelcomeScreen(
    onNavigateToLogin: () -> Unit, // Acción cuando presiona "Iniciar sesión" o "Registrar"
    onRegisterClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(// Fondo con gradiente vertical (de arriba hacia abajo)
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0A2E2F),
                        Color(0xFF1FA3A3),
                        Color(0xFF4ED2C4)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),// margen lateral
            horizontalAlignment = Alignment.CenterHorizontally,   // centra horizontalmente
            verticalArrangement = Arrangement.Center
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),// Bordes redondeados
                colors = CardDefaults.cardColors(containerColor = White), // Color de fondo de la tarjeta
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)// Sombra (elevación)
            ) {
                Column(// Contenido interno de la tarjeta
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "¡Bienvenido a Vita App!",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = PineBlue
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Elige una opción para continuar",
                        style = MaterialTheme.typography.bodyMedium // Usa estilo del tema
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = onNavigateToLogin,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PineBlue)
                    ) {
                        Text("Iniciar sesión", color = White)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = onRegisterClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PineBlue)
                    ) {
                        Text("Registrarse", color = White)
                    }
                }
            }
        }
    }
}