package com.example.vita_app.ui.components

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vita_app.data.remote.model.DiaryEntryResponse
import com.example.vita_app.ui.theme.CarbonBlack


@Composable
fun MealSection(
    section: String,
    entries: List<DiaryEntryResponse> = emptyList(),
    onAddClick: () -> Unit = {},
    onEntryDelete: (DiaryEntryResponse) -> Unit = {},
    onEntryClick: (DiaryEntryResponse) -> Unit = {}
) {
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)) {
        Text(
            section,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(Modifier.height(8.dp))

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(1.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                entries.forEach { entry ->
                    key(entry.id) { EntryRow(entry, onEntryDelete, onEntryClick) }
                }

                Row(
                    modifier = Modifier.fillMaxWidth()
                        .clickable { onAddClick() }
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Add, contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary)
                    Spacer(Modifier.width(8.dp))
                    Text("Agregar comida", color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}

@Composable
private fun EntryRow(
    entry: DiaryEntryResponse,
    onDelete: (DiaryEntryResponse) -> Unit,
    onClick: (DiaryEntryResponse) -> Unit
) {
    //Estadi del gesto de deslizar (lo calcula cada vez que cambia)
    val dismissState = rememberSwipeToDismissBoxState()
    //Se re-ejecuta cuando el valor del swipe cambia
    LaunchedEffect(dismissState.currentValue) {
        //Si se deslizo de der a izquierda, se dispara el borrado
        if (dismissState.currentValue == SwipeToDismissBoxValue.EndToStart) onDelete(entry)
    }

    //Valores del entry
    val per100 = entry.meal.calories.toDoubleOrNull() ?: 0.0
    val grams = entry.grams.toDoubleOrNull() ?: 0.0
    val totalCal = (per100 * grams / 100.0).toInt()

    SwipeToDismissBox(
        state = dismissState, //Estado que se observa
        enableDismissFromStartToEnd = false, //Permite deslizar solo en una direccion
        backgroundContent = {
            Box(
                modifier = Modifier.fillMaxSize()
                    .background(MaterialTheme.colorScheme.errorContainer)
                    .padding(horizontal = 20.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(Icons.Default.Delete, "Eliminar",
                    tint = MaterialTheme.colorScheme.onErrorContainer)
            }
        }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .clickable { onClick(entry) }
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Restaurant, contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                }
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(entry.meal.name, style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface)
                    Text("${grams.toInt()} g", style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            Text("$totalCal cal", style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}