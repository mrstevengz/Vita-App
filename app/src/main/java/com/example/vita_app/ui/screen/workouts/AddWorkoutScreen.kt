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
import androidx.compose.material3.Button
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
import com.example.vita_app.ui.components.AppBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddWorkoutScreen(
    viewModel: WorkoutViewModel,
    workoutId: Int,
    onAdd: () -> Unit,
    onBack: () -> Unit
) {
    val workout = viewModel.workouts.find {it.id == workoutId}

    if(workout == null) {
        Text("Workout not found")
        return
    }

    //Estado local
    var minutes by remember{ mutableStateOf("") }
    var submit by remember {mutableStateOf(false)}

    //Valores derivados
    val perHour = workout.caloriesPerHour.toDoubleOrNull() ?: 0.0
    val mins = minutes.toDoubleOrNull() ?: 0.0
    val burned = (perHour * mins / 60.0).toInt()

    AppBackground {
        Column(modifier = Modifier.fillMaxSize()) {
            CenterAlignedTopAppBar(
                title = { Text("Agregar workout") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Navegar hacia atras"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )

            Column(
                modifier = Modifier.fillMaxWidth().padding(16.dp).verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Ejercicio elegido (solo lectura — referencia al catálogo)
                Text(workout.name, style = MaterialTheme.typography.titleMedium)
                Text("${workout.caloriesPerHour} cal/h", color = Color.Gray)

                // Minutos hechos
                OutlinedTextField(
                    value = minutes,
                    onValueChange = { minutes = it },
                    label = { Text("Minutes") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                // Preview vivo de calorías quemadas (derivado de 'minutes')
                Text("Quemarás ~$burned cal", color = Color.Gray)

                // Add: crea la entrada de workout y vuelve atrás
                Button(
                    enabled = !submit && minutes.isNotBlank(),
                    onClick = {
                        submit = true
                        viewModel.addEntry(workout.id, minutes)
                        onAdd()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Add")
                }
            }
        }
    }
}
