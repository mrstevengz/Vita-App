package com.example.vita_app.ui.screen.meals

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
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
import com.example.vita_app.data.remote.model.MealType
import com.example.vita_app.ui.components.AppBackground
import com.example.vita_app.ui.components.FormScaffold
import com.example.vita_app.ui.components.MacroPreview
import com.example.vita_app.ui.components.SelectedItemHeader
import com.example.vita_app.ui.util.label

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMeal(viewModel: MealsViewModel, mealId: Int, onMealAdd: () -> Unit, onBack: () -> Unit, initialSection: MealType) {
    val meal = viewModel.meals.find { it.id == mealId } ?: return

    var grams by remember { mutableStateOf("") }
    var section by remember { mutableStateOf(initialSection) }
    var submit by remember { mutableStateOf(false) }

    FormScaffold(title = "Agregar comida", onBack = onBack) {
        SelectedItemHeader(Icons.Default.Restaurant, meal.name, "${meal.calories} cal / 100g")

        OutlinedTextField(
            value = grams,
            onValueChange = { grams = it },
            label = { Text("Gramos") },
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Text("Sección", style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurface)
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            MealType.entries.forEach { type ->
                FilterChip(section == type, onClick = { section = type }, label = { Text(type.label()) })
            }
        }

        MacroPreview(meal = meal, grams = grams)

        Button(
            enabled = !submit && grams.isNotBlank(),
            onClick = { submit = true; viewModel.addEntry(meal.id, grams, section); onMealAdd() },
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth().height(52.dp)
        ) { Text("Agregar") }
    }
}
