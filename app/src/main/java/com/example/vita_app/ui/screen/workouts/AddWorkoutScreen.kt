package com.example.vita_app.ui.screen.workouts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FitnessCenter
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
import com.example.vita_app.ui.components.FormScaffold
import com.example.vita_app.ui.components.PreviewCard
import com.example.vita_app.ui.components.SelectedItemHeader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddWorkoutScreen(viewModel: WorkoutViewModel, workoutId: Int, onAdd: () -> Unit, onBack: () -> Unit) {
    val workout = viewModel.workouts.find { it.id == workoutId } ?: return

    var minutes by remember { mutableStateOf("") }
    var submit by remember { mutableStateOf(false) }
    val burned = ((workout.caloriesPerHour.toDoubleOrNull() ?: 0.0) *
            (minutes.toDoubleOrNull() ?: 0.0) / 60.0).toInt()

    FormScaffold(title = "Agregar ejercicio", onBack = onBack) {
        SelectedItemHeader(Icons.Default.FitnessCenter, workout.name, "${workout.caloriesPerHour} cal/h")

        OutlinedTextField(
            value = minutes,
            onValueChange = { minutes = it },
            label = { Text("Minutos") },
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        PreviewCard("Se quemará", "$burned cal")

        Button(
            enabled = !submit && minutes.isNotBlank(),
            onClick = { submit = true; viewModel.addEntry(workout.id, minutes); onAdd() },
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth().height(52.dp)
        ) { Text("Agregar") }
    }
}