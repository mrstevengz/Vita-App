package com.example.vita_app.ui.components

// Proposito: Componente superior de Home con saludo y acceso visual al perfil.

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vita_app.R
import com.example.vita_app.ui.theme.CarbonBlack

@Composable
// Barra superior reutilizable que saluda al usuario.
fun HomeTopBar(nombre: String) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 35.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Column {
            Text(
                text = "Bienvenido,",
                fontSize = 18.sp,
                color = Color.Gray
            )
            Text(
                text = nombre,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = CarbonBlack
            )
        }

        //  LOGO (se usa Image y painter (painter resource para la ubicacion del logo))
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo App",
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
        )
    }
}