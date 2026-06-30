package com.example.vita_app.ui.screen.workouts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.vita_app.ui.components.AppBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditWorkoutScreen(
    viewmodel: WorkoutViewModel,
    entryId: Int,
    onCompleted: () -> Unit,
    onBack: () -> Unit
) {
    val entry = viewmodel.entries.find { it.id == entryId }
    if (entry == null) {
        Text("Entry not found")
        return
    }

    // Pre-rellenado con los minutos actuales
    var minutes by remember { mutableStateOf(entry.minutes) }

    // Preview vivo de calorías quemadas (derivado de 'minutes')
    val perHour = entry.workout.caloriesPerHour.toDoubleOrNull() ?: 0.0
    val mins = minutes.toDoubleOrNull() ?: 0.0
    val burned = (perHour * mins / 60.0).toInt()

    val saveWorkout = {
        viewmodel.updateEntry(entryId, minutes)
        onCompleted()
    }

    AppBackground {
        Column(modifier = Modifier.fillMaxSize()) {
            CenterAlignedTopAppBar(
                title = { Text("Editar workout") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Navegar hacia atras")
                    }
                },
                actions = {
                    IconButton(onClick = saveWorkout) {
                        Icon(Icons.Default.CheckCircle, contentDescription = "Confirmar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )

            Column(
                modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState()).padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("Workout: ${entry.workout.name}", style = MaterialTheme.typography.titleMedium)

                OutlinedTextField(
                    value = minutes,
                    onValueChange = { minutes = it },
                    label = { Text("Minutes") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Text("Quemarás ~$burned cal", color = Color.Gray)
            }
        }
    }
}