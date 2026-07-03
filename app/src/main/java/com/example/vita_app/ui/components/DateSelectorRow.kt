package com.example.vita_app.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun DateSelectorRow(
    date: LocalDate,
    onPrev: () -> Unit,
    onNext: () -> Unit,
    onToday: () -> Unit
) {
    val today = LocalDate.now()
    val label = when (date) {
        today -> "Hoy" //Si es hoy se escribe
        today.minusDays(1) -> "Ayer" //Si es ayer se escribe
        else -> date.format(DateTimeFormatter.ofPattern("EEE d MMM")) //Si no, fecha completa
    }
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onPrev) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, "Día anterior")
        }
        Text(
            text = label,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier.clickable { onToday() }   // tap en la fecha -> volver a HOY
        )
        IconButton(
            onClick = onNext,
            enabled = date.isBefore(today)                // no dejar navegar al futuro (logica)
        ) {
            Icon(Icons.AutoMirrored.Filled.ArrowForward, "Día siguiente")
        }
    }
}